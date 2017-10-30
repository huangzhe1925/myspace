package com.hz.myspace.cms.util.session.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.hz.myspace.cms.util.MSCMSEnvironmentPropertyUtil;
import com.hz.myspace.cms.util.scheduler.impl.SessionRefreshUpdaterScheduler;
import com.hz.myspace.cms.util.session.SessionSubject;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;


@Configuration
public class SessionManager {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private  Repository repository;

	@Autowired
	private MSCMSEnvironmentPropertyUtil envPropUtil;

	public static ThreadLocal<String> sessionKey = new ThreadLocal<String>();
	
	public static final String SESSION_KEY_ATTRIBUTE="sessionkey";

	protected Map<String, SessionSubject> sessionMap=new HashMap<String, SessionSubject>();

	public Session getAdminSession() throws LoginException, RepositoryException {
		Session adminSession = null;
		String sessKey = sessionKey.get();
		if (StringUtils.isEmpty(sessKey)||null==sessionMap.get(sessKey)) {
			
			String adminUser = this.envPropUtil.getCms().get(MSCMSEnvironmentPropertyUtil.CMS_REPOSITORY_ADMIN_USER);
			String adminPaswd = this.envPropUtil.getCms().get(MSCMSEnvironmentPropertyUtil.CMS_REPOSITORY_ADMIN_PASSWORD);
			adminSession = this.repository
					.login(new SimpleCredentials(adminUser, adminPaswd.toCharArray()));
			SessionSubject sessSub = new SessionSubjectImpl(adminSession);
			sessSub.addSessionUpdater(new SessionRefreshUpdater());
			sessSub.addSessionUpdaterScheduler(
					new SessionRefreshUpdaterScheduler(adminSession, sessSub.getSessionUpdaters()));
			sessSub.executeSchedulers();

			Integer sessHash = adminSession.hashCode();
			logger.debug("New session created :" +sessHash);
			sessionKey.set(sessHash+"");
			sessionMap.put(sessHash+"", sessSub);
		} else {
			logger.debug("Old session retrieved :" +sessKey);
			adminSession = sessionMap.get(sessKey).getSession();
		}
		return adminSession;
	}

	public Session getAdminSession(Integer sessionKey) {
		if (null == sessionKey) {
			return null;
		}
		return sessionMap.get(sessionKey).getSession();
	}

	public void logOutCurrentSession() throws LoginException, RepositoryException {
		Object sessKey = sessionKey.get();
		if (null != sessKey) {
			logOutSession(sessKey.toString());
		}
	}

	public void logOutSession(String sessionKey) throws LoginException, RepositoryException {
		if (null == sessionKey) {
			return;
		}

		Session session = sessionMap.get(sessionKey).getSession();
		if (session.isLive()) {
			session.logout();
		}
	}

}

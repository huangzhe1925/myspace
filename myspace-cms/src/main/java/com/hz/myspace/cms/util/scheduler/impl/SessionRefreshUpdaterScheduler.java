package com.hz.myspace.cms.util.scheduler.impl;

import java.util.Collection;

import javax.jcr.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hz.myspace.cms.util.scheduler.Scheduler;
import com.hz.myspace.cms.util.scheduler.SessionUpdaterScheduler;
import com.hz.myspace.cms.util.session.SessionUpdater;
import com.hz.myspace.cms.util.session.impl.SessionRefreshUpdater;

public class SessionRefreshUpdaterScheduler extends SessionUpdaterScheduler implements Scheduler{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public SessionRefreshUpdaterScheduler(Session updateSession,Collection<SessionUpdater> sessionUpdaterList){
		super(updateSession,sessionUpdaterList);
	}
	
	@Override
	public void doSessionUpdaterSchedule() {
		
		logger.debug("Enter doSessionUpdaterSchedule");
		if(null==getSessionUpdaterList()||getSessionUpdaterList().isEmpty()){
			return ;
		}
		for(SessionUpdater sessUpda:getSessionUpdaterList()){
			if(sessUpda instanceof SessionRefreshUpdater){
				try {
					sessUpda.updateSession(getUpdateSession());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		logger.debug("EXIT doSessionUpdaterSchedule");
	}

}

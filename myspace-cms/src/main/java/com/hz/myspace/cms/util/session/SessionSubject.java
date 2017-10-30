package com.hz.myspace.cms.util.session;

import java.util.Collection;

import javax.jcr.Session;

import com.hz.myspace.cms.util.scheduler.SessionUpdaterScheduler;

public interface SessionSubject {
	
	public void setSession(Session session);
	
	public Session getSession();
	
	public void addSessionUpdater(SessionUpdater observer);
	
	public Collection<SessionUpdater> getSessionUpdaters();
	
	public void removeSessionUpdater(SessionUpdater observer);
	
	public void addSessionUpdaterScheduler(SessionUpdaterScheduler scheduler);
	
	public void removeSessionUpdaterScheduler(SessionUpdaterScheduler scheduler);
	
	public void notifySessionUpdaters(Session session);
	
	public void executeSchedulers();
	
	
}

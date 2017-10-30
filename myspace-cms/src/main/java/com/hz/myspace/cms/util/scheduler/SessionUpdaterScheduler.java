package com.hz.myspace.cms.util.scheduler;

import java.util.Collection;

import javax.jcr.Session;

import com.hz.myspace.cms.util.session.SessionUpdater;

public abstract class SessionUpdaterScheduler extends AbstractScheduler implements Scheduler{
	
	private Collection<SessionUpdater> sessionUpdaterList;
	private Session updateSession;
	
	public SessionUpdaterScheduler() {
		super();
	}

	public SessionUpdaterScheduler(Session updateSession,Collection<SessionUpdater> sessionUpdaterList) {
		super();
		this.updateSession=updateSession;
		this.sessionUpdaterList = sessionUpdaterList;
	}
	
	@Override
	public void doScheduleJob() {
		doSessionUpdaterSchedule();
	}
	
	public abstract void doSessionUpdaterSchedule();

	public Collection<SessionUpdater> getSessionUpdaterList() {
		return sessionUpdaterList;
	}

	public void setSessionUpdaterList(Collection<SessionUpdater> sessionUpdaterList) {
		this.sessionUpdaterList = sessionUpdaterList;
	}

	public Session getUpdateSession() {
		return updateSession;
	}

	public void setUpdateSession(Session updateSession) {
		this.updateSession = updateSession;
	}
	
	
	
	
}

package com.hz.myspace.cms.util.session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.jcr.Session;

import com.hz.myspace.cms.util.scheduler.SessionUpdaterScheduler;

public abstract class AbstractSessionSubject implements SessionSubject{
	
	protected Session session;

	protected List<SessionUpdater> sessUpdaList=new ArrayList<SessionUpdater>();
	
	protected List<SessionUpdaterScheduler> SessUpdaScheList=new ArrayList<SessionUpdaterScheduler>();
	
	private ScheduledExecutorService schedulerPool=Executors.newScheduledThreadPool(10);
	
	public AbstractSessionSubject(){
	}
	
	public AbstractSessionSubject(Session session){
		this.session=session;
	}
	
	public abstract void notifySessionUpdaters();
	
	@Override
	public void notifySessionUpdaters(Session session) {
		for(SessionUpdater sessObj: this.sessUpdaList){
			try {
				sessObj.updateSession(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	@Override
	public void executeSchedulers() {
		for(SessionUpdaterScheduler scheduler:SessUpdaScheList){
			schedulerPool.scheduleAtFixedRate(scheduler, 0, SessionUpdaterScheduler.DEFAULT_PERIOD_DURATION,TimeUnit.MILLISECONDS);
		}
	}
	
	@Override
	public void setSession(Session session) {
		this.session=session;
	}

	@Override
	public Session getSession() {
		return this.session;
	}
	

	@Override
	public Collection<SessionUpdater> getSessionUpdaters() {
		return this.sessUpdaList;
	}
	
	@Override
	public void addSessionUpdater(SessionUpdater observer) {
		sessUpdaList.add(observer);
	}

	@Override
	public void removeSessionUpdater(SessionUpdater observer) {
		sessUpdaList.remove(observer);		
	}
	
	@Override
	public void addSessionUpdaterScheduler(SessionUpdaterScheduler scheduler) {
		SessUpdaScheList.add(scheduler);
	}

	@Override
	public void removeSessionUpdaterScheduler(SessionUpdaterScheduler scheduler) {
		SessUpdaScheList.remove(scheduler);
	}
	
}

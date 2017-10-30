package com.hz.myspace.cms.util.session.impl;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import com.hz.myspace.cms.util.session.SessionUpdater;

public class SessionRefreshUpdater implements SessionUpdater{

	@Override
	public boolean updateSession(Session session) throws RepositoryException {
		if(session!=null&&session.isLive()){
			session.refresh(true);
			return true;
		}
		return false;
	}

}

package com.hz.myspace.cms.util.session.impl;

import javax.jcr.Session;

import com.hz.myspace.cms.util.session.AbstractSessionSubject;
import com.hz.myspace.cms.util.session.SessionSubject;

public class SessionSubjectImpl extends AbstractSessionSubject implements SessionSubject{

	public SessionSubjectImpl(Session session){
		super(session);
	}

	@Override
	public void notifySessionUpdaters() {
		notifySessionUpdaters(this.session);
	}

}

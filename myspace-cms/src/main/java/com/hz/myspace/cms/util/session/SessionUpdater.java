package com.hz.myspace.cms.util.session;

import javax.jcr.Session;

public interface SessionUpdater {
	public boolean updateSession(Session session) throws Exception;
}

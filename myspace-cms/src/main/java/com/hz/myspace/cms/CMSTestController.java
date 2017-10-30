package com.hz.myspace.cms;

import javax.jcr.Repository;
import javax.jcr.Session;

import org.apache.jackrabbit.api.JackrabbitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hz.myspace.cms.util.MSCMSEnvironmentPropertyUtil;
import com.hz.myspace.cms.util.session.impl.SessionManager;

@Controller
@RequestMapping(value = "cmstest")
public class CMSTestController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public MSCMSEnvironmentPropertyUtil propertyUtil;

	@Autowired
	private SessionManager sessionManager;
	
	@Autowired
	private  Repository repository;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "testprop")
	@ResponseBody
	public String getEnvProp() {
		
		String retStr=MSCMSEnvironmentPropertyUtil.printMapValues(propertyUtil.getCommon(),propertyUtil.getCms());
		
		logger.info(retStr);
		return retStr;
	}
	
	@RequestMapping(value = "testrepo")
	@ResponseBody
	public String getRootNode(){
		String retVal="error";
		try {
			Session adminSession=sessionManager.getAdminSession();
			retVal=adminSession.getRootNode().getPath();
			if(!adminSession.nodeExists("/test")){
				adminSession.getRootNode().addNode("test");
				adminSession.save();
				retVal+=" added node test";
			}else{
				retVal+=" node test exists";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
	@RequestMapping(value = "close")
	@ResponseBody
	public String closeRepo(){
		String retVal="error";
		try {
			if (repository instanceof JackrabbitRepository) {
				((JackrabbitRepository) repository).shutdown();
				logger.info("Repository shutdown complete");
				retVal="Repository shutdown complete";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

}

package com.hz.myspace.cms.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.hz.myspace.common.utils.MSEnvironmentPropertyUtil;

@Configuration
@PropertySources({ 
	@PropertySource(value = "classpath:config/application.properties",ignoreResourceNotFound=true), 
	@PropertySource(value = "classpath:config/application-${spring.profiles.active}.properties",ignoreResourceNotFound=true), 
})
@ConfigurationProperties(prefix="ms")
public class MSCMSEnvironmentPropertyUtil extends MSEnvironmentPropertyUtil {
	
	public static final String CMS_REPOSITORY_HOME="repository.home";
	public static final String  CMS_REPOSITORY_ADMIN_USER="repository.admin.user";
	public static final String  CMS_REPOSITORY_ADMIN_PASSWORD="repository.admin.password";
	
	public static final String  CMS_TEMP_DIR_IMAGES="tempdir.images";
	
	private Map<String, String> cms = new HashMap<String, String>();

	public Map<String, String> getCms() {
		return cms;
	}

	public void setCms(Map<String, String> cms) {
		this.cms = cms;
	}


}

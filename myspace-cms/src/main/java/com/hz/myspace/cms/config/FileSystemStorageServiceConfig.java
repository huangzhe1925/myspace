package com.hz.myspace.cms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hz.myspace.cms.util.MSCMSEnvironmentPropertyUtil;
import com.hz.myspace.common.fileupload.FileSystemStorageService;
import com.hz.myspace.common.fileupload.StorageService;

@Configuration
public class FileSystemStorageServiceConfig {
	
	// Declare StorageService
	@Autowired
	@Bean(name="storageService")
	public StorageService storageService(MSCMSEnvironmentPropertyUtil properties) {
		String tempDirImages=properties.getCms().get(MSCMSEnvironmentPropertyUtil.CMS_TEMP_DIR_IMAGES);
		StorageService  service = new FileSystemStorageService(tempDirImages);
		service.deleteAll();
		service.init();
		return service;
	}
}

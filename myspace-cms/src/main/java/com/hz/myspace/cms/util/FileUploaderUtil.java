package com.hz.myspace.cms.util;

import javax.jcr.Binary;
import javax.jcr.Session;
import javax.jcr.ValueFactory;

import org.springframework.core.io.Resource;

public class FileUploaderUtil {
	
	public static String gernerateFullFileName(String nodePath,String nodeName,String nodeProperyName,String fileName){
	
		nodePath=nodePath.replace('/', '.').substring(1);
		
		String fullFileName = nodePath+"."+nodeName+"."+nodeProperyName+"-"+fileName;
		
		return fullFileName;
	}
	
	public static String gernerateFullFileName(String newNodePath,String nodeProperyName,String fileName){
	
		newNodePath=newNodePath.replace('/', '.').substring(1);
		
		String fullFileName = newNodePath+"."+nodeProperyName+"-"+fileName;
		
		return fullFileName;
	}
	
	public static Binary convertResourceToBinary(Resource resource,Session session) throws Exception{
		ValueFactory valueFactory = session.getValueFactory();
		Binary binFile = valueFactory.createBinary(resource.getInputStream());
		return binFile;
	}
}

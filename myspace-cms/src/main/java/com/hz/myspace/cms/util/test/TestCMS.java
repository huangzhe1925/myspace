package com.hz.myspace.cms.util.test;

import java.io.File;
import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.segment.SegmentNodeStore;
import org.apache.jackrabbit.oak.segment.SegmentNodeStoreBuilders;
import org.apache.jackrabbit.oak.segment.file.FileStore;
import org.apache.jackrabbit.oak.segment.file.FileStoreBuilder;
import org.apache.jackrabbit.oak.segment.file.InvalidFileStoreVersionException;

public class TestCMS {
	public static void main(String[] args) throws Exception {
		
		Repository repo=getRepo("D:/jackrabbit/oak");
		Session adminSession1 = getAdminSession(repo);

		System.out.println(getNodeStringProperty(adminSession1,"hello","count"));

//		getUserManager(adminSession).createUser("roger", "roger");
		
//		Node node=getNode(adminSession,"/oak:index/nodetype");
		
//		createNode(adminSession,"/hello","hello1");
//		System.out.println(node.getPath()+"  "+node.getName());
		
		adminSession1.logout();
		adminSession1.save();

		
		Thread.sleep(5000);
//		Session rogerSession=getUserSession(repository,"roger","roger");
//		System.out.println(getNodeStringProperty(rogerSession,"hello","count"));

	}
	
	public static Repository getRepo(String repoPath) throws InvalidFileStoreVersionException, IOException{
		File file = new File(repoPath);
		FileStore fs = FileStoreBuilder.fileStoreBuilder(file).build();
		SegmentNodeStore ns = SegmentNodeStoreBuilders.builder(fs).build();
		return new Jcr(new Oak(ns)).createRepository();
	}
	
	public static Node createNode(Session session,String nodePath,String newNodeName) throws Exception{
		
		Node node=null;
		if(isNodeExist(session,nodePath)){
			Node existNode= session.getNode(nodePath);
			if(existNode.hasNode(newNodeName)){
				node=existNode.getNode(newNodeName);
			}else{
				node=existNode.addNode(newNodeName);
				System.out.println("created node "+nodePath+"/"+newNodeName);
			}
		}
		
		return node;
	}

	public static String getNodeStringProperty(Session session, String nodePath, String propertyName) throws Exception {

		Node root = getRootNode(session);
		String retValue = null;

		if (root.hasNode(nodePath)) {
			Node node = root.getNode(nodePath);
			if (node.hasProperty(propertyName)) {
				retValue = node.getProperty(propertyName).getValue().getString();
			}
		}

		return retValue;
	}
	
	private static Boolean isNodeExist(Session session,String nodePath) throws RepositoryException{
		return session.nodeExists(nodePath);
		
	}

	private static Session getUserSession(Repository repository, SimpleCredentials credentials) throws Exception {
		Session session = repository.login(credentials);
		return session;
	}

	private static Session getUserSession(Repository repository, String userName, String userPasswd) throws Exception {
		Session session = repository.login(new SimpleCredentials(userName, userPasswd.toCharArray()));
		return session;
	}

	private static Session getAdminSession(Repository repository) throws Exception {
		Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
		return session;
	}

	private static Node getRootNode(Session session) throws Exception {
		return session.getRootNode();
	}
	
	private static Node getNode(Session session,String nodePath) throws Exception {
		return session.getNode(nodePath);
	}

	private static UserManager getUserManager(Session adminSession) throws Exception {

		UserManager um = null;
		if (adminSession instanceof JackrabbitSession) {
			um = ((JackrabbitSession) adminSession).getUserManager();
		}

		return um;
	}

	public static void startNewRepo() {

		Thread newThread = new Thread(new Runnable() {
			@Override
			public void run() {
				File file = new File("D:/jackrabbit/oak");
				System.out.println(file.getAbsolutePath());
				FileStore fs = null;
				try {
					fs = FileStoreBuilder.fileStoreBuilder(file).build();

					SegmentNodeStore ns = SegmentNodeStoreBuilders.builder(fs).build();
					Repository repository = new Jcr(new Oak(ns)).createRepository();

					Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
					Node root = session.getRootNode();

					if (root.hasNode("hello")) {
						Node hello = root.getNode("hello");
						long count = hello.getProperty("count").getLong();
						hello.setProperty("count", count + 1);
						System.out.println("found the hello node in startNewRepo, count = " + count);
					} else {
						System.out.println("creating the hello node");
						root.addNode("hello").setProperty("count", 1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		newThread.start();
	}

}

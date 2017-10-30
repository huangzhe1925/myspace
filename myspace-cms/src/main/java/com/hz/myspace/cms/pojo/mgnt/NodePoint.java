package com.hz.myspace.cms.pojo.mgnt;

import java.util.List;

public class NodePoint {

	private String nodePath;
	private String nodeName;

	private List<NodePoint> childNodes;

	private List<PropertyPoint> properties;

	public List<NodePoint> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(List<NodePoint> childNodes) {
		this.childNodes = childNodes;
	}

	public List<PropertyPoint> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyPoint> properties) {
		this.properties = properties;
	}

	public String getNodePath() {
		return nodePath;
	}

	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

}

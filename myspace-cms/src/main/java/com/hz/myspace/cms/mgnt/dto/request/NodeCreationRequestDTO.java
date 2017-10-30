package com.hz.myspace.cms.mgnt.dto.request;

import java.util.List;

import com.hz.myspace.cms.pojo.mgnt.PropertyPoint;

public class NodeCreationRequestDTO extends CommonRequestDTO {
	
	private String nodePath;
	private String nodeName;
	private List<PropertyPoint> propList;

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

	public List<PropertyPoint> getPropList() {
		return propList;
	}

	public void setPropList(List<PropertyPoint> propList) {
		this.propList = propList;
	}

}

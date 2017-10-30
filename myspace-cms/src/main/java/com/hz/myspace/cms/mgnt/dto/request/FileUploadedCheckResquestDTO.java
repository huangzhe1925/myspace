package com.hz.myspace.cms.mgnt.dto.request;

public class FileUploadedCheckResquestDTO extends CommonRequestDTO {
	private String nodePath;
	private String nodeName;
	private String nodePropertyName;
	private String fileName;

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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getNodePropertyName() {
		return nodePropertyName;
	}

	public void setNodePropertyName(String nodePropertyName) {
		this.nodePropertyName = nodePropertyName;
	}

}

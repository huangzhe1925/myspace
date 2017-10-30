package com.hz.myspace.cms.mgnt.dto.response;

public class FileUploadResponseDTO extends CommonResponseDTO{
	private String uploadedFileName;

	public String getUploadedFileName() {
		return uploadedFileName;
	}

	public void setUploadedFileName(String uploadedFileName) {
		this.uploadedFileName = uploadedFileName;
	}
}

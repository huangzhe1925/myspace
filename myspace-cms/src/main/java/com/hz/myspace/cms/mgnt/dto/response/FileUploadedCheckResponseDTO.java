package com.hz.myspace.cms.mgnt.dto.response;

public class FileUploadedCheckResponseDTO extends CommonResponseDTO{
	private String uploadedFileName;
	private String uploadedFileFullName;
	private Boolean isFileUploaded;

	public String getUploadedFileName() {
		return uploadedFileName;
	}

	public void setUploadedFileName(String uploadedFileName) {
		this.uploadedFileName = uploadedFileName;
	}

	public String getUploadedFileFullName() {
		return uploadedFileFullName;
	}

	public void setUploadedFileFullName(String uploadedFileFullName) {
		this.uploadedFileFullName = uploadedFileFullName;
	}

	public Boolean getIsFileUploaded() {
		return isFileUploaded;
	}

	public void setIsFileUploaded(Boolean isFileUploaded) {
		this.isFileUploaded = isFileUploaded;
	}
}

package com.hz.myspace.cms.mgnt.dto.response;

import java.util.List;

import com.hz.myspace.cms.exceptions.ExceptionCode;

public class CommonResponseDTO {
	private Boolean isSuccessful;
	List<ExceptionCode> list;
	private String message;

	public Boolean getIsSuccessful() {
		return isSuccessful;
	}

	public void setIsSuccessful(Boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}

	public List<ExceptionCode> getList() {
		return list;
	}

	public void setList(List<ExceptionCode> list) {
		this.list = list;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

package com.hz.myspace.cms.exceptions;

public class ExceptionCode extends Exception {

	private String exceptionMessage;
	private String exceptionDescription;

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
}

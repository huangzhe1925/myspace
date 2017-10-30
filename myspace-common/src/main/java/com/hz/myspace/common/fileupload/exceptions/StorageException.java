package com.hz.myspace.common.fileupload.exceptions;

public class StorageException extends RuntimeException {

	private static final long serialVersionUID = 7810320047761407064L;

	public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
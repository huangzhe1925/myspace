package com.hz.myspace.common.fileupload.exceptions;

public class StorageFileNotFoundException extends StorageException {

	private static final long serialVersionUID = 9124040466310771442L;

	public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
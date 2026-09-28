package com.muller_tomas.reading_groups.exception;

public class FileStorageException extends RuntimeException {
	
	public FileStorageException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public FileStorageException(String message) {
		super(message);
	}

}

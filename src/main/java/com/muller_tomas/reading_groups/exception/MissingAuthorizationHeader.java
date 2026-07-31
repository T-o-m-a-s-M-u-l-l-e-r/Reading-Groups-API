package com.muller_tomas.reading_groups.exception;

public class MissingAuthorizationHeader extends RuntimeException {

	public MissingAuthorizationHeader(String message) {
		super(message);
	}
}

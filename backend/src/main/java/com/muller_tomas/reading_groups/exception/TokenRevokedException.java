package com.muller_tomas.reading_groups.exception;

public class TokenRevokedException extends RuntimeException {
	public TokenRevokedException(String message) {
		super(message);
	}
}
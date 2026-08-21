package com.muller_tomas.reading_groups.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(long timestamp, int status, String message, String path, String details) {
	
	public ApiErrorResponse(int status, String message, String path) {
		this(System.currentTimeMillis(), status, message, path, null);
	}
}

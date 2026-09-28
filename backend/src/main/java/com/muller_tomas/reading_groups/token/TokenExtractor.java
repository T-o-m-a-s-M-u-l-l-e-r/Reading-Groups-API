package com.muller_tomas.reading_groups.token;

import org.springframework.stereotype.Component;

import com.muller_tomas.reading_groups.exception.MissingAuthorizationHeader;

@Component
public class TokenExtractor {

	public String extractTokenFromHeader(String header) throws MissingAuthorizationHeader {

		if (header == null || header.isBlank() || !header.startsWith("Bearer ")) {
			throw new MissingAuthorizationHeader("Missing or invalid Authorization header");
		}

		return header.substring(7);
	}

}

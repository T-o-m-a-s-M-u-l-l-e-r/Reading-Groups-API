package com.muller_tomas.reading_groups.token;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Component
public class TokenExtractor {

	public String extractTokenFromHeader(String header) throws BadCredentialsException {

		if (header == null || header.isBlank() || !header.startsWith("Bearer ")) {
			throw new BadCredentialsException("Missing or invalid Authorization header");
		}

		return header.substring(7);
	}

}

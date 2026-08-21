package com.muller_tomas.reading_groups.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.muller_tomas.reading_groups.exception.MissingAuthorizationHeader;

public class TokenExtractorTest {
	private TokenExtractor tokenExtractor;
	private String exampleToken;
	
	@BeforeEach
	public void setUp() {
		tokenExtractor = new TokenExtractor();
		exampleToken = "test-token";
	}
	
	@Test
	public void extractToken_shouldThrowException_whenHeaderIsMissing() {
		assertThrows(MissingAuthorizationHeader.class, () -> tokenExtractor.extractTokenFromHeader(""));
	}
	
	@Test
	public void extractToken_shouldThrowException_whenHeaderIsBlank() {
		assertThrows(MissingAuthorizationHeader.class, () -> tokenExtractor.extractTokenFromHeader("   	"));
	}
	
	@Test
	public void extractToken_shouldThrowException_whenHeaderIsNull() {
		assertThrows(MissingAuthorizationHeader.class, () -> tokenExtractor.extractTokenFromHeader(null));
	}
	
	@Test
	public void extractToken_shouldThrowException_whenHeaderLacksBearer() {
		assertThrows(MissingAuthorizationHeader.class, () -> tokenExtractor.extractTokenFromHeader("Bear %s".formatted(exampleToken)));
		
	}
	
	@Test
	public void extractToken_ShouldReturnToken_whenHeaderIsValid() {
		String header = "Bearer %s".formatted(exampleToken);
		String extractedToken = tokenExtractor.extractTokenFromHeader(header);
		assertEquals(exampleToken, extractedToken);
	}

}

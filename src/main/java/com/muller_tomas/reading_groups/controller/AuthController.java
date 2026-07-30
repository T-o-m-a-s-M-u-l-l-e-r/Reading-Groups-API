package com.muller_tomas.reading_groups.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.muller_tomas.reading_groups.dto.AccessTokenResponse;
import com.muller_tomas.reading_groups.dto.LoginRequest;
import com.muller_tomas.reading_groups.dto.TokenPairResponse;
import com.muller_tomas.reading_groups.service.AuthService;
import com.muller_tomas.reading_groups.token.TokenExtractor;

@RestController
public class AuthController {
	private AuthService authService;
	private TokenExtractor tokenExtractor;

	public AuthController(AuthService authService, TokenExtractor tokenExtractor) {
		this.authService = authService;
		this.tokenExtractor = tokenExtractor;
	}

	@PostMapping("/api/auth/login")
	public ResponseEntity<TokenPairResponse> login(@RequestBody LoginRequest loginRequest) {
		TokenPairResponse tokenPairResponse = authService.loginUser(loginRequest);
		return ResponseEntity.ok(tokenPairResponse);
	}

	@PostMapping("/api/auth/refresh")
	public ResponseEntity<AccessTokenResponse> refreshToken(@RequestHeader("Authorization") String refreshToken) {
		String token = tokenExtractor.extractTokenFromHeader(refreshToken);
		String accessToken = authService.refreshToken(token);
		return ResponseEntity.ok(new AccessTokenResponse(accessToken));
	}

	@PostMapping("/api/auth/logout")
	public ResponseEntity<Void> logout(@RequestHeader("Authorization") String refreshToken) {
		String token = tokenExtractor.extractTokenFromHeader(refreshToken);
		authService.logoutUser(token);
		return ResponseEntity.noContent().build();
	}

}

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Authentication", description = "Endpoints for user login and token management")
@RestController
public class AuthController {
	private final AuthService authService;
	private final TokenExtractor tokenExtractor;

	public AuthController(AuthService authService, TokenExtractor tokenExtractor) {
		this.authService = authService;
		this.tokenExtractor = tokenExtractor;
	}
	@PostMapping("/api/auth/login")
	@Operation(summary = "Authenticate user", description = "Validate an email or username and password and return an access token and refresh token.")
	@ApiResponse(responseCode = "200", description = "Authentication successful; token pair returned")
	@ApiResponse(responseCode = "400", description = "Invalid request fields")
	@ApiResponse(responseCode = "401", description = "Invalid credentials")
	@ApiResponse(responseCode = "404", description = "User not found")
	public ResponseEntity<TokenPairResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		TokenPairResponse tokenPairResponse = authService.loginUser(loginRequest);
		return ResponseEntity.ok(tokenPairResponse);
	}
	@PostMapping("/api/auth/refresh")
	@Operation(summary = "Refresh access token", description = "Validate a refresh token supplied in the Authorization header and issue a new access token.")
	@ApiResponse(responseCode = "200", description = "New access token returned")
	@ApiResponse(responseCode = "400", description = "Missing or malformed Authorization header, or incorrect token type")
	@ApiResponse(responseCode = "401", description = "Invalid refresh token")
	@ApiResponse(responseCode = "403", description = "Refresh token has been revoked")
	public ResponseEntity<AccessTokenResponse> refreshToken(@RequestHeader("Authorization") String refreshToken) {
		String token = tokenExtractor.extractTokenFromHeader(refreshToken);
		String accessToken = authService.refreshToken(token);
		return ResponseEntity.ok(new AccessTokenResponse(accessToken));
	}
	@PostMapping("/api/auth/logout")
	@Operation(summary = "Log out", description = "Revoke the refresh token supplied in the Authorization header.")
	@ApiResponse(responseCode = "204", description = "Refresh token revoked")
	@ApiResponse(responseCode = "400", description = "Missing or malformed Authorization header, or incorrect token type")
	@ApiResponse(responseCode = "401", description = "Invalid refresh token")
	@ApiResponse(responseCode = "403", description = "Refresh token already revoked")
	public ResponseEntity<Void> logout(@RequestHeader("Authorization") String refreshToken) {
		String token = tokenExtractor.extractTokenFromHeader(refreshToken);
		authService.logoutUser(token);
		return ResponseEntity.noContent().build();
	}

}

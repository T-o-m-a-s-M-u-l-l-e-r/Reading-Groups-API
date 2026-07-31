package com.muller_tomas.reading_groups.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/api/auth")
@RestController
public class AuthController {
	private AuthService authService;
	private TokenExtractor tokenExtractor;

	public AuthController(AuthService authService, TokenExtractor tokenExtractor) {
		this.authService = authService;
		this.tokenExtractor = tokenExtractor;
	}

	@PostMapping("/login")
	@Operation(summary = "Authenticate user login", description = "Validates credentials (username/email and password) and returns an access and a refresh token")
    @ApiResponse(responseCode = "200", description = "Login successful")
	@ApiResponse(responseCode = "400", description = "Validation failed (e.g., empty fields")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
	public ResponseEntity<TokenPairResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		TokenPairResponse tokenPairResponse = authService.loginUser(loginRequest);
		return ResponseEntity.ok(tokenPairResponse);
	}

	@PostMapping("/refresh")
	@Operation(summary = "Request a new access token", description = "Validates refresh token and returns an access token")
    @ApiResponse(responseCode = "200", description = "Access token returned")
	@ApiResponse(responseCode = "400", description = "Invalid Authorization header")
    @ApiResponse(responseCode = "401", description = "Invalid refresh token")
	@ApiResponse(responseCode = "403", description = "Refresh token revoked")
	public ResponseEntity<AccessTokenResponse> refreshToken(@RequestHeader("Authorization") String refreshToken) {
		String token = tokenExtractor.extractTokenFromHeader(refreshToken);
		String accessToken = authService.refreshToken(token);
		return ResponseEntity.ok(new AccessTokenResponse(accessToken));
	}

	@PostMapping("/logout")
	@Operation(summary = "Logout user", description = "Performs logout based on refresh token")
    @ApiResponse(responseCode = "204", description = "Successful logout")
	@ApiResponse(responseCode = "400", description = "Invalid Authorization header")
    @ApiResponse(responseCode = "401", description = "Invalid refresh token")
	@ApiResponse(responseCode = "403", description = "Refresh token revoked")
	public ResponseEntity<Void> logout(@RequestHeader("Authorization") String refreshToken) {
		String token = tokenExtractor.extractTokenFromHeader(refreshToken);
		authService.logoutUser(token);
		return ResponseEntity.noContent().build();
	}

}

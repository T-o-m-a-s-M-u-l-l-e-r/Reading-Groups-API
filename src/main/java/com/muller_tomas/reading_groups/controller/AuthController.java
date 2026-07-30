package com.muller_tomas.reading_groups.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.muller_tomas.reading_groups.TokenRevokedException;
import com.muller_tomas.reading_groups.config.JwtTokenProvider;
import com.muller_tomas.reading_groups.config.JwtTokenProvider.TokenType;
import com.muller_tomas.reading_groups.dto.AccessTokenResponse;
import com.muller_tomas.reading_groups.dto.LoginRequest;
import com.muller_tomas.reading_groups.dto.RegisterRequest;
import com.muller_tomas.reading_groups.dto.TokenPairResponse;
import com.muller_tomas.reading_groups.service.UserService;

@RestController
public class AuthController {
	private JwtTokenProvider tokenProvider;
	private UserService userService;

	public AuthController(JwtTokenProvider tokenProvider, UserService userService) {
		this.tokenProvider = tokenProvider;
		this.userService = userService;
	}

	@PostMapping("/api/auth/login")
	public ResponseEntity<TokenPairResponse> login(@RequestBody LoginRequest loginRequest)
			throws UsernameNotFoundException, BadCredentialsException {
		TokenPairResponse tokenPairResponse = userService.loginUser(loginRequest);
		return ResponseEntity.ok(tokenPairResponse);
	}

	@PostMapping("/api/auth/register")
	public ResponseEntity<TokenPairResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
		TokenPairResponse tokenPairResponse = userService.createUser(registerRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(tokenPairResponse);
	}

	@PostMapping("/api/auth/refresh")
	public ResponseEntity<AccessTokenResponse> refreshToken(@RequestHeader("Authorization") String refreshToken)
			throws BadCredentialsException, TokenRevokedException {
		String token = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		boolean blacklisted = tokenProvider.isRefreshTokenBlacklisted(token);

		if (!blacklisted) {
			int userId = tokenProvider.getUserId(token);
			String accessToken = tokenProvider.createToken(userId, TokenType.ACCESS);
			return ResponseEntity.ok(new AccessTokenResponse(accessToken));
		} else {
			throw new TokenRevokedException("Refresh token has been revoked");
		}

	}

	@PostMapping("/api/auth/logout")
	public ResponseEntity<Void> logout(@RequestHeader("Authorization") String refreshToken) {
		String token = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		tokenProvider.blacklistRefreshToken(token);
		return ResponseEntity.noContent().build();
	}

}

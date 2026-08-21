package com.muller_tomas.reading_groups.service;

import java.util.HashSet;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.muller_tomas.reading_groups.dto.LoginRequest;
import com.muller_tomas.reading_groups.dto.TokenPairResponse;
import com.muller_tomas.reading_groups.exception.InvalidTokenTypeException;
import com.muller_tomas.reading_groups.exception.TokenRevokedException;
import com.muller_tomas.reading_groups.model.User;
import com.muller_tomas.reading_groups.token.JwtTokenProvider;
import com.muller_tomas.reading_groups.token.JwtTokenProvider.TokenType;

@Service
public class AuthService {
	private final JwtTokenProvider tokenProvider;
	private final UserService userService;
	private final BCryptPasswordEncoder passwordEncoder;
	private HashSet<String> refreshTokenBlacklist;

	public AuthService(UserService userService, BCryptPasswordEncoder passwordEncoder,
			JwtTokenProvider tokenProvider) {
		this.userService = userService;
		this.tokenProvider = tokenProvider;
		this.passwordEncoder = passwordEncoder;
		refreshTokenBlacklist = new HashSet<String>();
	}
	
	public String refreshToken(String token) {
		
		if (!tokenProvider.isTokenValid(token)) {
			throw new BadCredentialsException("Token is invalid");
		}
		
		if (tokenProvider.getTokenType(token) != TokenType.REFRESH) {
			throw new InvalidTokenTypeException("Invalid token type");
		}
		
		if (!refreshTokenBlacklist.contains(token)) {
			int userId = tokenProvider.getUserId(token);
			String accessToken = tokenProvider.createToken(userId, TokenType.ACCESS);
			return accessToken;
		} else {
			throw new TokenRevokedException("Refresh token has been revoked");
		}
		
	}
	
	public void logoutUser(String refreshToken) {
		
		if (!tokenProvider.isTokenValid(refreshToken)) {
			throw new BadCredentialsException("Token is invalid");
		}
		
		TokenType tokenType = tokenProvider.getTokenType(refreshToken);
		
		if (tokenType != TokenType.REFRESH) {
			throw new InvalidTokenTypeException("Invalid token type");
		}
		
		if (!refreshTokenBlacklist.contains(refreshToken)) {
			refreshTokenBlacklist.add(refreshToken);
		} else {
			throw new TokenRevokedException("Token has been revoked");
		}
		
	}

	public TokenPairResponse loginUser(LoginRequest loginRequest)
			throws UsernameNotFoundException, BadCredentialsException {
		User user = userService.findUserByUsernameOrEmail(loginRequest.getLogin());
		boolean isPasswordCorrect = passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash());

		if (isPasswordCorrect) {
			int userId = user.getId();
			String accessToken = tokenProvider.createToken(userId, TokenType.ACCESS);
			String refreshToken = tokenProvider.createToken(userId, TokenType.REFRESH);
			TokenPairResponse tokenResponse = new TokenPairResponse(accessToken, refreshToken);
			return tokenResponse;
		} else {
			throw new BadCredentialsException("Bad credentials");
		}

	}

}

package com.muller_tomas.reading_groups.config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
	private SecretKey signingKey;
	private HashSet<String> refreshTokenBlacklist;

	public enum TokenType {
		ACCESS, REFRESH;

		public long getValidityInMilliseconds() {

			return switch (this) {
			case ACCESS -> 900000;
			case REFRESH -> 604800000;
			};

		}

	}

	public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
		refreshTokenBlacklist = new HashSet<String>();

		try {
			byte[] keyBytes = MessageDigest.getInstance("SHA-256").digest(secret.getBytes(StandardCharsets.UTF_8));
			signingKey = Keys.hmacShaKeyFor(keyBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 not available", e);
		}

	}

	public void blacklistRefreshToken(String refreshToken) {
		refreshTokenBlacklist.add(refreshToken);
	}

	public String createToken(int userId, TokenType tokenType) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + tokenType.getValidityInMilliseconds());

		return Jwts.builder().subject(String.valueOf(userId)).issuedAt(now).expiration(validity).signWith(signingKey)
				.compact();
	}

	public int getUserId(String token) {
		Claims claims = Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
		return Integer.valueOf(claims.getSubject());
	}

	public boolean isTokenValid(String token) {

		try {
			Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}

	}

	public boolean isRefreshTokenBlacklisted(String refreshToken) {
		return refreshTokenBlacklist.contains(refreshToken);
	}
	
	public String extractTokenFromHeader(String header) throws BadCredentialsException {

		if (header == null || header.isBlank() || !header.startsWith("Bearer ")) {
			throw new BadCredentialsException("Missing or invalid Authorization header");
		}

		return header.substring(7);
	}

}

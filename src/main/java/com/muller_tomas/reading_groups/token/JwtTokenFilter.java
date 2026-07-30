package com.muller_tomas.reading_groups.token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.muller_tomas.reading_groups.exception.InvalidTokenTypeException;
import com.muller_tomas.reading_groups.token.JwtTokenProvider.TokenType;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
	private JwtTokenProvider jwtTokenProvider;
	private List<String> publicPaths;
	private TokenExtractor tokenExtractor;

	public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, @Value("${app.public.paths}") List<String> publicPaths, TokenExtractor tokenExtractor) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.publicPaths = publicPaths;
		this.tokenExtractor = tokenExtractor;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();

		return publicPaths.stream().anyMatch(publicPath -> {
			if (publicPath.endsWith("/**")) {
				String prefix = publicPath.substring(0, publicPath.length() - 3);
				return path.startsWith(prefix);
			}
			return path.equals(publicPath);
		});
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			String header = request.getHeader("Authorization");
			String token = tokenExtractor.extractTokenFromHeader(header);

			boolean valid = jwtTokenProvider.isTokenValid(token);
			TokenType tokenType;

			try {
				tokenType = jwtTokenProvider.getTokenType(token);
			} catch (IllegalArgumentException e) {
				throw new InvalidTokenTypeException("Invalid token type");
			}

			if (!valid) {
				throw new BadCredentialsException("Invalid token");
			}

			if (tokenType != TokenType.ACCESS) {
				throw new InvalidTokenTypeException("Invalid token type");
			}

			int userId = jwtTokenProvider.getUserId(token);
			Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (BadCredentialsException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
			return;
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
			return;
		}

		filterChain.doFilter(request, response);
	}

}

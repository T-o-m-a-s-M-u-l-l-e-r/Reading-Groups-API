package com.muller_tomas.reading_groups.config;

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

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
	private JwtTokenProvider jwtTokenProvider;
	private List<String> publicPaths;

	public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, @Value("${app.public.paths}") List<String> publicPaths) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.publicPaths = publicPaths;
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
			String token = jwtTokenProvider.extractTokenFromHeader(header);

			boolean valid = jwtTokenProvider.isTokenValid(token);

			if (valid) {
				Authentication authentication = new UsernamePasswordAuthenticationToken(token, null, new ArrayList<>());
				SecurityContextHolder.getContext().setAuthentication(authentication);
				filterChain.doFilter(request, response);
			} else {
				throw new BadCredentialsException("Invalid token");
			}

		} catch (BadCredentialsException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
		}

	}

}

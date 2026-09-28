package com.muller_tomas.reading_groups.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.muller_tomas.reading_groups.token.JwtTokenFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final JwtTokenFilter jwtTokenFilter;
	private final List<String> publicPaths;

	public SecurityConfig(JwtTokenFilter jwtTokenFilter, @Value("${app.public.paths}") List<String> publicPaths) {
		this.jwtTokenFilter = jwtTokenFilter;
		this.publicPaths = publicPaths;
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable());
		http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authorizeHttpRequests(auth -> auth.requestMatchers(publicPaths.toArray(new String[0])).permitAll()
				.anyRequest().authenticated());
		http.exceptionHandling(ex -> ex.accessDeniedHandler(
				(request, response, exn) -> response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied")));
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();

	}

}
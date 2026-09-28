package com.muller_tomas.reading_groups.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.muller_tomas.reading_groups.TestDataFactory;
import com.muller_tomas.reading_groups.dto.LoginRequest;
import com.muller_tomas.reading_groups.model.User;
import com.muller_tomas.reading_groups.token.JwtTokenProvider;
import com.muller_tomas.reading_groups.token.JwtTokenProvider.TokenType;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
	@Mock
	private JwtTokenProvider tokenProvider;
	
	@Mock
	private UserService userService;
	
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	
	private AuthService authService;
	
	@BeforeEach
	public void setUp() {
		authService = new AuthService(userService, passwordEncoder, tokenProvider);
	}
	
	@Test
	public void loginUser_shouldReturnTokenPairResponse_whenRequestIsValid() {
		LoginRequest loginRequest = new LoginRequest(TestDataFactory.exampleUsername, TestDataFactory.examplePassword);
		User loginUser = TestDataFactory.getSavedUser();
		 
		when(userService.findUserByUsernameOrEmail(loginRequest.getLogin())).thenReturn(loginUser);
		when(passwordEncoder.matches(loginRequest.getPassword(), loginUser.getPasswordHash())).thenReturn(true);
		when(tokenProvider.createToken(loginUser.getId(), TokenType.ACCESS)).thenReturn(TestDataFactory.exampleAccessToken);
		when(tokenProvider.createToken(loginUser.getId(), TokenType.REFRESH)).thenReturn(TestDataFactory.exampleRefreshToken);
		
		assertEquals(TestDataFactory.getTokenPairResponse(), authService.loginUser(loginRequest));
	}
	
	@Test
	public void loginUser_shouldThrowBadCredentialsException_whenPasswordIsInvalid() {
		String wrongPassword = TestDataFactory.examplePassword + "d";
		LoginRequest loginRequest = new LoginRequest(TestDataFactory.exampleUsername, wrongPassword);
		User savedUser = TestDataFactory.getSavedUser();
		 
		when(userService.findUserByUsernameOrEmail(loginRequest.getLogin())).thenReturn(savedUser);
		when(passwordEncoder.matches(loginRequest.getPassword(), savedUser.getPasswordHash())).thenReturn(false);
		
		assertThrows(BadCredentialsException.class, () -> {authService.loginUser(loginRequest);});
	}

}

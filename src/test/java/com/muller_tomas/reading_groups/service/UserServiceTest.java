package com.muller_tomas.reading_groups.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.muller_tomas.reading_groups.dto.RegisterRequest;
import com.muller_tomas.reading_groups.dto.TokenPairResponse;
import com.muller_tomas.reading_groups.exception.DuplicateUserException;
import com.muller_tomas.reading_groups.model.Group;
import com.muller_tomas.reading_groups.model.User;
import com.muller_tomas.reading_groups.repository.UserRepository;
import com.muller_tomas.reading_groups.token.JwtTokenProvider;
import com.muller_tomas.reading_groups.token.JwtTokenProvider.TokenType;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	private UserService userService;
	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Mock
	private UserRepository userRepository;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	private String exampleEmail, exampleUsername, examplePassword, exampleAccessToken, exampleRefreshToken, examplePasswordHash;
	private int exampleId;

	@BeforeEach
	public void setUp() {
		userService = new UserService(userRepository, passwordEncoder, jwtTokenProvider);
		exampleEmail = "email@gmail.com";
		exampleUsername = "steve2";
		examplePassword = "Password123";
		examplePasswordHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
		exampleId = 1;
		exampleAccessToken = "test-access-token";
		exampleRefreshToken = "test-refresh-token";
	}

	@Test
	public void createUser_shouldThrowDuplicateUserException_whenUsernameExists() {
		User duplicatedUser = new User(exampleId, exampleUsername, "email2@gmail.com", examplePasswordHash,
				new HashSet<Group>());
		RegisterRequest registerRequest = new RegisterRequest(exampleEmail, exampleUsername, examplePassword);

		when(userRepository.findByUsername(exampleUsername)).thenReturn(Optional.of(duplicatedUser));
		assertThrows(DuplicateUserException.class, () -> userService.createUser(registerRequest));
	}

	@Test
	public void createUser_shouldThrowDuplicateUserException_whenEmailExists() {
		User duplicatedUser = new User(exampleId, "john5", exampleEmail, examplePasswordHash, new HashSet<Group>());
		RegisterRequest registerRequest = new RegisterRequest(exampleEmail, exampleUsername, examplePassword);

		when(userRepository.findByEmail(exampleEmail)).thenReturn(Optional.of(duplicatedUser));
		assertThrows(DuplicateUserException.class, () -> userService.createUser(registerRequest));
	}

	@Test
	public void createUser_shouldReturnTokenPairResponse_whenRequestIsValid() {
		RegisterRequest registerRequest = new RegisterRequest(exampleEmail, exampleUsername, examplePassword);

		when(userRepository.findByUsername(exampleUsername)).thenReturn(Optional.empty());
		when(userRepository.findByEmail(exampleEmail)).thenReturn(Optional.empty());
		when(passwordEncoder.encode(examplePassword)).thenReturn(examplePasswordHash);

		User createdUser = new User(exampleId, exampleUsername, exampleEmail, examplePasswordHash,
				new HashSet<Group>());
		when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(createdUser);

		when(jwtTokenProvider.createToken(createdUser.getId(), TokenType.ACCESS)).thenReturn(exampleAccessToken);
		when(jwtTokenProvider.createToken(createdUser.getId(), TokenType.REFRESH)).thenReturn(exampleRefreshToken);

		TokenPairResponse tokenPair = userService.createUser(registerRequest);
		
		verify(userRepository, times(1)).findByUsername(exampleUsername);
		verify(userRepository, times(1)).findByEmail(exampleEmail);
		verify(userRepository, times(1)).save(ArgumentMatchers.any(User.class));
		
		assertEquals(new TokenPairResponse(exampleAccessToken, exampleRefreshToken), tokenPair);
	}

}

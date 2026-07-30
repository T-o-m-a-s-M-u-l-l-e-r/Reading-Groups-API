package com.muller_tomas.reading_groups.service;

import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.muller_tomas.reading_groups.DuplicateUserException;
import com.muller_tomas.reading_groups.config.JwtTokenProvider;
import com.muller_tomas.reading_groups.config.JwtTokenProvider.TokenType;
import com.muller_tomas.reading_groups.dto.LoginRequest;
import com.muller_tomas.reading_groups.dto.RegisterRequest;
import com.muller_tomas.reading_groups.dto.TokenPairResponse;
import com.muller_tomas.reading_groups.model.User;
import com.muller_tomas.reading_groups.repository.UserRepository;

@Service
public class UserService {
	private JwtTokenProvider jwtTokenProvider;
	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
			JwtTokenProvider jwtTokenProvider) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public TokenPairResponse loginUser(LoginRequest loginRequest)
			throws UsernameNotFoundException, BadCredentialsException {
		User user = findUserByUsernameOrEmail(loginRequest.getLogin())
				.orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));
		boolean isPasswordCorrect = passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash());

		if (isPasswordCorrect) {
			int userId = user.getId();
			String accessToken = jwtTokenProvider.createToken(userId, TokenType.ACCESS);
			String refreshToken = jwtTokenProvider.createToken(userId, TokenType.REFRESH);
			TokenPairResponse tokenResponse = new TokenPairResponse(accessToken, refreshToken);
			return tokenResponse;
		} else {
			throw new BadCredentialsException("Bad credentials");
		}

	}

	public TokenPairResponse createUser(RegisterRequest registerRequest) throws DuplicateUserException {

		if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()
				|| userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
			throw new DuplicateUserException("Duplicate user");
		}

		User user = new User();
		user.setEmail(registerRequest.getEmail());
		user.setUsername(registerRequest.getUsername());

		String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
		user.setPasswordHash(hashedPassword);

		User createdUser = userRepository.save(user);
		int userId = createdUser.getId();

		String refreshToken = jwtTokenProvider.createToken(userId, TokenType.REFRESH);
		String accessToken = jwtTokenProvider.createToken(userId, TokenType.ACCESS);

		return new TokenPairResponse(accessToken, refreshToken);
	}

	public Optional<User> findUserByUsernameOrEmail(String login) {
		return userRepository.findByUsername(login).or(() -> userRepository.findByEmail(login));
	}

}

package com.muller_tomas.reading_groups.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.muller_tomas.reading_groups.dto.RegisterRequest;
import com.muller_tomas.reading_groups.dto.TokenPairResponse;
import com.muller_tomas.reading_groups.exception.DuplicateUserException;
import com.muller_tomas.reading_groups.exception.UserNotFoundException;
import com.muller_tomas.reading_groups.model.User;
import com.muller_tomas.reading_groups.repository.UserRepository;
import com.muller_tomas.reading_groups.token.JwtTokenProvider;
import com.muller_tomas.reading_groups.token.JwtTokenProvider.TokenType;

@Service
public class UserService {
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
			JwtTokenProvider jwtTokenProvider) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public TokenPairResponse createUser(RegisterRequest registerRequest) throws DuplicateUserException {

		if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
			throw new DuplicateUserException("There already exists a user with the specified username");
		}

		if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
			throw new DuplicateUserException("There already exists a user with the specified email");
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

	public User findUserByUsernameOrEmail(String login) throws UsernameNotFoundException {
		return userRepository.findByUsername(login).or(() -> userRepository.findByEmail(login))
				.orElseThrow(() -> new UserNotFoundException("User with specified login not found"));
	}

}

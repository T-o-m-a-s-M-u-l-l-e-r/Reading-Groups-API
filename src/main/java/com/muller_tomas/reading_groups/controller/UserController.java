package com.muller_tomas.reading_groups.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.muller_tomas.reading_groups.dto.RegisterRequest;
import com.muller_tomas.reading_groups.dto.TokenPairResponse;
import com.muller_tomas.reading_groups.service.UserService;

@RestController
public class UserController {
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/api/users/register")
	public ResponseEntity<TokenPairResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
		TokenPairResponse tokenPairResponse = userService.createUser(registerRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(tokenPairResponse);
	}

}

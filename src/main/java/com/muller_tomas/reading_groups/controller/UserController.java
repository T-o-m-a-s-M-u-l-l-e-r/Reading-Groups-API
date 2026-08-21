package com.muller_tomas.reading_groups.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.muller_tomas.reading_groups.dto.RegisterRequest;
import com.muller_tomas.reading_groups.dto.TokenPairResponse;
import com.muller_tomas.reading_groups.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Users", description = "Endpoints for user operations")
@RequestMapping("/api/users")
@RestController
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/register")
	@Operation(summary = "Register new user", description = "Register new user based on email, username and password")
	@ApiResponse(responseCode = "201", description = "Registration successful")
	@ApiResponse(responseCode = "400", description = "Validation failed (e.g., empty fields, invalid email)")
	@ApiResponse(responseCode = "409", description = "User already exists")
	public ResponseEntity<TokenPairResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
		TokenPairResponse tokenPairResponse = userService.createUser(registerRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(tokenPairResponse);
	}

}

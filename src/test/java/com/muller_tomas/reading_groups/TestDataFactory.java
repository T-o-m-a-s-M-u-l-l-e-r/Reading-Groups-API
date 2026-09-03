package com.muller_tomas.reading_groups;

import java.util.HashSet;

import com.muller_tomas.reading_groups.dto.LoginRequest;
import com.muller_tomas.reading_groups.dto.RegisterRequest;
import com.muller_tomas.reading_groups.dto.TokenPairResponse;
import com.muller_tomas.reading_groups.model.Group;
import com.muller_tomas.reading_groups.model.User;

public class TestDataFactory {
	public static final String exampleEmail = "email@gmail.com";
	public static final String exampleUsername = "steve2";
	public static final String examplePassword = "P@ssword123";
	public static final String examplePasswordHash = "example-password-hash";
	public static final int exampleId = 1;
	public static final String exampleAccessToken = "example-access-token";
	public static final String exampleRefreshToken = "example-refresh-token";
	
	public static User getCreatedUser() {
		return new User(exampleUsername, exampleEmail, examplePasswordHash);
	}
	
	public static User getSavedUser() {
		return new User(exampleId, exampleUsername, exampleEmail, examplePasswordHash, new HashSet<Group>());
	}
	
	public static User getSavedUserWithEmail(String email) {
		return new User(exampleId, exampleUsername, email, examplePasswordHash, new HashSet<Group>());
	}
	
	public static RegisterRequest getRegisterRequest() {
		return new RegisterRequest(exampleEmail, exampleUsername, examplePassword);
	}
	
	public static TokenPairResponse getTokenPairResponse() {
		return new TokenPairResponse(exampleAccessToken, exampleRefreshToken);
	}
	
}

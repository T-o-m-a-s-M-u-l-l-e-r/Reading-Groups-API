package com.muller_tomas.reading_groups.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

	@NotBlank(message = "Email cannot be empty")
	@Email(message = "Email must be valid")
	private String email;

	@NotBlank(message = "Username cannot be empty")
	private String username;

	@NotBlank(message = "Password cannot be empty")
	@Size(min = 6, message = "Password must be at least 8 characters")
	private String password;

	public RegisterRequest(String email, String username, String password) {
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

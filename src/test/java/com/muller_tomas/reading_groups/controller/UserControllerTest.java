package com.muller_tomas.reading_groups.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.muller_tomas.reading_groups.TestDataFactory;
import com.muller_tomas.reading_groups.dto.RegisterRequest;
import com.muller_tomas.reading_groups.dto.TokenPairResponse;
import com.muller_tomas.reading_groups.exception.DuplicateUserException;
import com.muller_tomas.reading_groups.service.UserService;
import com.muller_tomas.reading_groups.token.JwtTokenFilter;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(value = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
	@MockitoBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private JwtTokenFilter jwtTokenFilter;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void registerUser_shouldReturn200WithTokenPair_whenRequestIsValid() throws Exception {
		RegisterRequest registerRequest = TestDataFactory.getRegisterRequest();
		TokenPairResponse tokenPairResponse = TestDataFactory.getTokenPairResponse();

		when(userService.createUser(registerRequest)).thenReturn(tokenPairResponse);

		String jsonInput = objectMapper.writeValueAsString(registerRequest);

		mockMvc.perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content(jsonInput))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.accessToken").value(tokenPairResponse.getAccessToken()))
				.andExpect(jsonPath("$.refreshToken").value(tokenPairResponse.getRefreshToken()));
	}

	@Test
	public void registerUser_shouldReturn400_whenUsernameIsBlank() throws Exception {
		String username = "	  ";
		RegisterRequest registerRequest = new RegisterRequest(TestDataFactory.exampleEmail, username,
				TestDataFactory.examplePassword);

		String jsonInput = objectMapper.writeValueAsString(registerRequest);

		mockMvc.perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content(jsonInput))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void registerUser_shouldReturn409_whenUserAlreadyExists() throws Exception {
		RegisterRequest registerRequest = TestDataFactory.getRegisterRequest();
		String jsonInput = objectMapper.writeValueAsString(registerRequest);
		String errorMessage = "There already exists a user with the specified username";

		doThrow(new DuplicateUserException(errorMessage)).when(userService)
				.createUser(any(RegisterRequest.class));

		mockMvc.perform(post("/api/users/register").contentType(MediaType.APPLICATION_JSON).content(jsonInput))
				.andExpect(status().isConflict())
				.andDo(result -> {assertEquals(errorMessage, result.getResponse().getContentAsString());});
	}

}

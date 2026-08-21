package com.muller_tomas.reading_groups.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.muller_tomas.reading_groups.dto.GroupCreationRequest;
import com.muller_tomas.reading_groups.dto.GroupResponse;
import com.muller_tomas.reading_groups.dto.UserResponse;
import com.muller_tomas.reading_groups.service.GroupService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Groups", description = "Endpoints for group management")
@RequestMapping("/api/groups")
@RestController
public class GroupController {
	private final GroupService groupService;

	public GroupController(GroupService groupService) {
		this.groupService = groupService;
	}

	@GetMapping("/get_user_groups")
	@Operation(summary = "Get current user's groups", description = "Retrieve all groups that the user is a member of")
	@ApiResponse(responseCode = "200", description = "Retrieval successful")
	@ApiResponse(responseCode = "404", description = "User not found")
	public ResponseEntity<Set<GroupResponse>> getUserGroups() {
		int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(groupService.getUserGroups(userId));
	}

	@GetMapping("/get_group_members")
	@Operation(summary = "Get group members", description = "Retrieve members of a group the user is part of")
	@ApiResponse(responseCode = "200", description = "Retrieval successful")
	@ApiResponse(responseCode = "401", description = "User is not part of the group")
	@ApiResponse(responseCode = "404", description = "User or group not found")
	public ResponseEntity<Set<UserResponse>> getGroupMembers(@RequestParam int groupId) {
		int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(groupService.getGroupMembers(userId, groupId));
	}

	@PostMapping(path = "/create_group", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Create a reading group", description = "Create a reading group with specified title and administrator")
	@ApiResponse(responseCode = "201", description = "Group created successfully")
	@ApiResponse(responseCode = "400", description = "Invalid reading text")
	@ApiResponse(responseCode = "500", description = "Internal server error handling file")
	public ResponseEntity<GroupResponse> createGroup(@Valid @ModelAttribute GroupCreationRequest groupCreationRequest) {
		int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		GroupResponse groupResponse = groupService.createGroup(userId, groupCreationRequest);
		return new ResponseEntity<GroupResponse>(groupResponse, HttpStatus.CREATED);
	}

}

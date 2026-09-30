package com.muller_tomas.reading_groups.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.muller_tomas.reading_groups.dto.InviteResponse;
import com.muller_tomas.reading_groups.service.InviteService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Invites", description = "Endpoints for invite operations")
@RestController
public class InviteController {
	private final InviteService inviteService;

	public InviteController(InviteService inviteService) {
		this.inviteService = inviteService;
	}

	@GetMapping("/api/invites")
	public ResponseEntity<Set<InviteResponse>> getUserInvites() {
		int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(inviteService.getUserInvites(userId));
	}

	@PostMapping("/api/groups/{groupId}/invites")
	public ResponseEntity<InviteResponse> sendInvite(@PathVariable int groupId, @RequestParam int userId) {
		int administratorId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		InviteResponse inviteResponse = inviteService.createInvite(administratorId, groupId, userId);
		return new ResponseEntity<InviteResponse>(inviteResponse, HttpStatus.CREATED);
	}

	@DeleteMapping("/api/invites/{inviteId}")
	public ResponseEntity<Void> rejectInvite(@PathVariable int inviteId) {
		int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		inviteService.deleteInvite(userId, inviteId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/api/invites/{inviteId}/accept")
	public ResponseEntity<Void> acceptInvite(@PathVariable int inviteId) {
		int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		inviteService.acceptInvite(userId, inviteId);
		return ResponseEntity.noContent().build();
	}

}

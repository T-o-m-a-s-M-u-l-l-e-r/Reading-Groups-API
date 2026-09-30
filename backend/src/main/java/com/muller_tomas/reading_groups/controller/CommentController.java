package com.muller_tomas.reading_groups.controller;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.muller_tomas.reading_groups.dto.CommentRequest;
import com.muller_tomas.reading_groups.dto.CommentResponse;
import com.muller_tomas.reading_groups.service.CommentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Comments", description = "Endpoints for comment management")
@RestController
public class CommentController {
	private final CommentService commentService;
	
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@GetMapping("/api/groups/{groupId}/comments")
	public ResponseEntity<Set<CommentResponse>> getGroupComments(@PathVariable int groupId) {
		int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Set<CommentResponse> comments = commentService.getGroupComments(userId, groupId);
		return ResponseEntity.ok(comments);
	}

	@GetMapping("/api/comments/{commentId}")
	public ResponseEntity<CommentResponse> getComment(@PathVariable int commentId) {
		int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(commentService.getComment(userId, commentId));
	}

	@DeleteMapping("/api/comments/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable int commentId) {
		int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		commentService.deleteComment(userId, commentId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/api/groups/{groupId}/comments")
	public ResponseEntity<CommentResponse> addComment(@PathVariable int groupId,
			@RequestBody @Valid CommentRequest commentRequest) {
		int userId = (int) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		CommentResponse commentResponse = commentService.addComment(userId, groupId, commentRequest);
		return new ResponseEntity<CommentResponse>(commentResponse, HttpStatus.CREATED);
	}

}

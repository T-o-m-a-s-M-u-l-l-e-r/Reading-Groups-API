package com.muller_tomas.reading_groups.service;

import java.util.Set;

import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muller_tomas.reading_groups.dto.CommentRequest;
import com.muller_tomas.reading_groups.dto.CommentResponse;
import com.muller_tomas.reading_groups.dto.MapperHelper;
import com.muller_tomas.reading_groups.exception.CommentNotFoundException;
import com.muller_tomas.reading_groups.model.Comment;
import com.muller_tomas.reading_groups.model.Group;
import com.muller_tomas.reading_groups.model.User;
import com.muller_tomas.reading_groups.repository.CommentRepository;

@Service
public class CommentService {
	private CommentRepository commentRepository;
	private GroupService groupService;
	private UserService userService;
	private MapperHelper mapperHelper;

	public CommentService(CommentRepository commentRepository, GroupService groupService, UserService userService,
			MapperHelper mapperHelper) {
		this.commentRepository = commentRepository;
		this.groupService = groupService;
		this.userService = userService;
		this.mapperHelper = mapperHelper;
	}

	@Transactional
	public CommentResponse addComment(int userId, int groupId, CommentRequest commentRequest) {
		User user = userService.findUserByUserId(userId);
		Group group = groupService.findGroupById(groupId);

		if (!group.getUsers().contains(user)) {
			throw new AuthorizationDeniedException("You are not authorized to access this group");
		}

		Comment newComment = new Comment();
		newComment.setTextSection(commentRequest.getTextSection());
		newComment.setCommentContent(commentRequest.getCommentContent());
		newComment.setCommentPageNumber(commentRequest.getCommentPageNumber());
		newComment.setCommentPageOccurrence(commentRequest.getCommentPageOccurrence());
		newComment.setUser(user);
		newComment.setGroup(group);

		Comment savedComment = commentRepository.save(newComment);
		return mapperHelper.mapComment(savedComment);
	}

	public void deleteComment(int userId, int commentId) {
		User user = userService.findUserByUserId(userId);
		Comment comment = findCommentByCommentId(commentId);

		if (!comment.getGroup().getAdministratorUser().equals(user) && !comment.getUser().equals(user)) {
			throw new AuthorizationDeniedException("You are not authorized to delete this comment");
		}

		commentRepository.delete(comment);
	}

	public Set<CommentResponse> getGroupComments(int userId, int groupId) {
		User user = userService.findUserByUserId(userId);
		Group group = groupService.findGroupById(groupId);

		if (!group.getUsers().contains(user)) {
			throw new AuthorizationDeniedException("You are not authorized to view this group");
		}

		Set<Comment> comments = commentRepository.findAllByGroup_Id(groupId);
		return mapperHelper.mapComments(comments);
	}

	public CommentResponse getComment(int userId, int commentId) {
		User user = userService.findUserByUserId(userId);
		Comment comment = findCommentByCommentId(commentId);

		if (!comment.getGroup().getUsers().contains(user)) {
			throw new AuthorizationDeniedException("You are not authorized to view this comment");
		}

		return mapperHelper.mapComment(comment);
	}

	public Comment findCommentByCommentId(int commentId) {
		return commentRepository.findById(commentId)
				.orElseThrow(() -> new CommentNotFoundException("Comment with specified id not found"));
	}

}

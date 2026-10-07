package com.muller_tomas.reading_groups.dto;

import java.time.LocalDateTime;

public class CommentResponse {
	private Integer commentId;
	private Integer groupId;
	private Integer categoryId;
	private Integer userId;
	private String textSection;
	private String commentContent;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Integer commentPageNumber;
	private Integer commentPageOccurrence;
	private String authorName;
	private Integer authorId;

	public CommentResponse() {
	}

	public CommentResponse(Integer commentId, Integer groupId, Integer categoryId, Integer userId, String textSection,
			String commentContent, LocalDateTime createdAt, LocalDateTime updatedAt, Integer commentPageNumber,
			Integer commentPageOccurrence, String authorName, Integer authorId) {
		this.commentId = commentId;
		this.groupId = groupId;
		this.categoryId = categoryId;
		this.userId = userId;
		this.textSection = textSection;
		this.commentContent = commentContent;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.commentPageNumber = commentPageNumber;
		this.commentPageOccurrence = commentPageOccurrence;
		this.authorName = authorName;
		this.authorId = authorId;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTextSection() {
		return textSection;
	}

	public void setTextSection(String textSection) {
		this.textSection = textSection;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getCommentPageNumber() {
		return commentPageNumber;
	}

	public void setCommentPageNumber(Integer commentPageNumber) {
		this.commentPageNumber = commentPageNumber;
	}

	public Integer getCommentPageOccurrence() {
		return commentPageOccurrence;
	}

	public void setCommentPageOccurrence(Integer commentPageOccurrence) {
		this.commentPageOccurrence = commentPageOccurrence;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public Integer getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

}

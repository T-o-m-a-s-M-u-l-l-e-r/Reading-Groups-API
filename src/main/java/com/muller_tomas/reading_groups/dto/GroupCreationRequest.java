package com.muller_tomas.reading_groups.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GroupCreationRequest {
	@NotBlank(message = "Group name cannot be empty")
	private String groupName;
	
	@NotNull(message ="Reading text cannot be empty")
	private MultipartFile readingText;
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public MultipartFile getReadingText() {
		return readingText;
	}
	public void setReadingText(MultipartFile readingText) {
		this.readingText = readingText;
	}
	
}

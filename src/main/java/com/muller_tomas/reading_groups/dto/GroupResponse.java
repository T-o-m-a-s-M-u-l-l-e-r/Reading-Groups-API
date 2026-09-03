package com.muller_tomas.reading_groups.dto;

import java.time.LocalDateTime;

public class GroupResponse {
	private int id;
	private int administratorUserId;
	private LocalDateTime createdAt;
	private String groupName;
	
	public GroupResponse(int id, int administratorUserId, LocalDateTime createdAt, String groupName) {
		super();
		this.id = id;
		this.administratorUserId = administratorUserId;
		this.createdAt = createdAt;
		this.groupName = groupName;
	}
	
	public GroupResponse() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAdministratorUserId() {
		return administratorUserId;
	}

	public void setAdministratorUserId(int administratorUserId) {
		this.administratorUserId = administratorUserId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}

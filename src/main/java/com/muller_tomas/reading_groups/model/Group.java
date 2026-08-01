package com.muller_tomas.reading_groups.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Groups")
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_id")
	private int id;

	@Column(name = "reading_text_path", length = 100, nullable = false)
	private String readingTextPath;

	@Column(name = "created_at", length = 100, nullable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name = "administrator_user_id", nullable = false)
	private int administratorUserId;
	
	@ManyToMany
    @JoinTable(
        name = "Users_Groups",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
	private List<User> users = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReadingTextPath() {
		return readingTextPath;
	}

	public void setReadingTextPath(String readingTextPath) {
		this.readingTextPath = readingTextPath;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public int getAdministratorUserId() {
		return administratorUserId;
	}

	public void setAdministratorUserId(int administratorUserId) {
		this.administratorUserId = administratorUserId;
	}

}

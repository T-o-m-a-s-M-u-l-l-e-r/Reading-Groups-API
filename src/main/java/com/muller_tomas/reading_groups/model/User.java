package com.muller_tomas.reading_groups.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int id;

	@Column(length = 25, nullable = false, unique = true)
	private String username;

	@Column(length = 100, nullable = false, unique = true)
	private String email;

	@Column(name = "password_hash", length = 100, nullable = false, unique = false)
	private String passwordHash;

	@ManyToMany(mappedBy = "users")
	private Set<Group> groups = new HashSet<>();

	public User() {
	}

	public User(int id, String username, String email, String passwordHash, HashSet<Group> groups) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.passwordHash = passwordHash;
		this.groups = groups;
	}

	public User(String username, String email, String passwordHash) {
		super();
		this.username = username;
		this.email = email;
		this.passwordHash = passwordHash;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

}

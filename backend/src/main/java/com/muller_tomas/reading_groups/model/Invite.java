package com.muller_tomas.reading_groups.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Invites", uniqueConstraints = { @UniqueConstraint(columnNames = { "group_id", "user_id" }) })
public class Invite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invite_id")
	private Integer inviteId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "group_id", nullable = false)
	private Group group;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User recipient;

	@Column(name = "sent_at", nullable = false, insertable = false, updatable = false)
	private LocalDateTime sentAt;
	
	public Invite() {
	}

	public Integer getInviteId() {
		return inviteId;
	}

	public void setInviteId(Integer inviteId) {
		this.inviteId = inviteId;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}

	public LocalDateTime getSentAt() {
		return sentAt;
	}

	public void setSentAt(LocalDateTime sentAt) {
		this.sentAt = sentAt;
	}
}
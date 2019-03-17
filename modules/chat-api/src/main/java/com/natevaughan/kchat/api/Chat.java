package com.natevaughan.kchat.api;

import java.time.Instant;
import java.util.List;

/**
 * Created by nate on 3/8/19
 */
public class Chat {

	private final Long id;
	private final Instant dateCreated;
	private final String name;
	private final String accessKey;
	private final User creator;
	private final List<User> participants;

	public Chat(Long id, Instant dateCreated, String name, String accessKey, User creator, List<User> participants) {
		this.id = id;
		this.dateCreated = dateCreated;
		this.name = name;
		this.accessKey = accessKey;
		this.creator = creator;
		this.participants = participants;
	}

	public Chat(String name, String accessKey, User creator, List<User> participants) {
		this.id = null;
		this.dateCreated = null;
		this.name = name;
		this.accessKey = accessKey;
		this.creator = creator;
		this.participants = participants;
	}

	public Long getId() {
		return id;
	}

	public Instant getDateCreated() {
		return dateCreated;
	}

	public String getName() {
		return name;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public User getCreator() {
		return creator;
	}

	public List<User> getParticipants() {
		return participants;
	}
}

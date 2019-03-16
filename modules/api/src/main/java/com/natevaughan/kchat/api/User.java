package com.natevaughan.kchat.api;

import java.security.Principal;
import java.time.Instant;

/**
 * Created by nate on 3/8/19
 */
public class User implements Principal {

	private final Long id;
	private final Instant dateCreated;
	private final String name;
	private final Role role;
	private final String apiKey;

	public User(Long id, Instant dateCreated, String name, Role role, String apiKey) {
		this.id = id;
		this.dateCreated = dateCreated;
		this.name = name;
		this.role = role;
		this.apiKey = apiKey;
	}

	public User(String name, Role role, String apiKey) {
		this.id = null;
		this.dateCreated = Instant.now();
		this.name = name;
		this.role = role;
		this.apiKey = apiKey;
	}

	public Long getId() {
		return id;
	}

	public Instant getDateCreated() {
		return dateCreated;
	}

	public Role getRole() {
		return role;
	}

	public String getApiKey() {
		return apiKey;
	}

	@Override
	public String getName() {
		return name;
	}

	public enum Role { USER, ADMIN }

	public User copy(String uuid) {
		return new User(id, Instant.now(), name, role, uuid);
	}
}

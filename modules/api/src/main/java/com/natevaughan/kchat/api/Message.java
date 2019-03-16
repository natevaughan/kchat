package com.natevaughan.kchat.api;

import java.time.Instant;

/**
 * Created by nate on 3/8/19
 */
public class Message {

	private final Long id;
	private final Instant dateCreated;
	private final String text;
	private final User author;
	private final Chat chat;
	private final Instant lastEdited;

	public Message(Long id, Instant dateCreated, String text, User author, Chat chat, Instant lastEdited) {
		this.id = id;
		this.dateCreated = dateCreated;
		this.text = text;
		this.author = author;
		this.chat = chat;
		this.lastEdited = lastEdited;
	}

	public Message(String text, User author, Chat chat) {
		this.id = null;
		this.dateCreated = null;
		this.text = text;
		this.author = author;
		this.chat = chat;
		this.lastEdited = null;
	}

	public Long getId() {
		return id;
	}

	public Instant getDateCreated() {
		return dateCreated;
	}

	public String getText() {
		return text;
	}
	public User getAuthor() {
		return author;
	}

	public Chat getChat() {
		return chat;
	}

	public Instant getLastEdited() {
		return lastEdited;
	}

	public Message copy(String text) {
		return new Message(id, dateCreated, text, author, chat, Instant.now());
	};
}

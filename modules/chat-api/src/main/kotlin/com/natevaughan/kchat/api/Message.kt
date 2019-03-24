package com.natevaughan.kchat.api

import com.fasterxml.jackson.annotation.JsonIgnore

import java.time.Instant

/**
 * Created by nate on 3/8/19
 */
class Message(val id: String, val dateCreated: Instant, val text: String, val author: User, @get:JsonIgnore val chat: Chat, lastEdited: Instant) {

	val lastEdited: Instant? = lastEdited

	fun copy(text: String): Message {
		return Message(id, dateCreated, text, author, chat, Instant.now())
	}
}

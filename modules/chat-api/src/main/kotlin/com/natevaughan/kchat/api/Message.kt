package com.natevaughan.kchat.api

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

import java.time.Instant

/**
 * Created by nate on 3/8/19
 */
class Message(
		val id: String,
		@get:JsonIgnore val dateCreated: Instant,
		val text: String,
		val author: User,
		@get:JsonIgnore val chat: Chat,
		@get:JsonIgnore private val lastEdited: Instant?) {

	@JsonProperty("lastEdited")
	fun lastEditedJson(): Long? {
		return lastEdited?.toEpochMilli()
	}

	@JsonProperty("dateCreated")
	fun dateCreatedJson(): Long? {
		return lastEdited?.toEpochMilli()
	}

	fun copy(text: String): Message {
		return Message(id, dateCreated, text, author, chat, Instant.now())
	}
}

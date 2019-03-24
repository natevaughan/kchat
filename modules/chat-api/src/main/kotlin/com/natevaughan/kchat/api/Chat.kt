package com.natevaughan.kchat.api

import com.fasterxml.jackson.annotation.JsonIgnore

import java.time.Instant

/**
 * Created by nate on 3/8/19
 */
class Chat {

	val id: String
	@get:JsonIgnore
	val dateCreated: Instant?
	val name: String?
	val space: Space?
	val type: Type?
	val participants: List<Participant>

	constructor(id: String, dateCreated: Instant, name: String, space: Space, type: Chat.Type, participants: List<Participant>) {
		this.id = id
		this.dateCreated = dateCreated
		this.name = name
		this.space = space
		this.type = type
		this.participants = participants
	}

	constructor(id: String) {
		this.id = id
		this.dateCreated = null
		this.name = null
		this.space = null
		this.type = null
		this.participants = emptyList()
	}

	enum class Type {
		PUBLIC_CHANNEL,
		PRIVATE_CHANNEL,
		CONVERSATION
	}

	fun copy(participants: List<Participant>): Chat {
		return Chat(this.id, this.dateCreated!!, this.name!!, this.space!!, this.type!!, participants)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Chat

		if (id != other.id) return false

		return true
	}

	override fun hashCode(): Int {
		return id.hashCode()
	}
}

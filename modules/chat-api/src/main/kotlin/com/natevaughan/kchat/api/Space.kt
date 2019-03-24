package com.natevaughan.kchat.api

import com.fasterxml.jackson.annotation.JsonIgnore

import java.time.Instant

/**
 * Created by nate on 3/20/19
 */
class Space {

	val id: String
	@get:JsonIgnore
	val dateCreated: Instant?
	val name: String?
	@get:JsonIgnore
	val users: Collection<Participant>

	constructor(id: String, dateCreated: Instant, name: String, users: Collection<Participant>) {
		this.id = id
		this.dateCreated = dateCreated
		this.name = name
		this.users = users
	}

	constructor(id: String) {
		this.id = id
		this.dateCreated = null
		this.name = null
		this.users = emptyList()
	}
}

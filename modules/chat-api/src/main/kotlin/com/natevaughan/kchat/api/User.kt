package com.natevaughan.kchat.api

import com.fasterxml.jackson.annotation.JsonIgnore

import java.security.Principal
import java.time.Instant

/**
 * Created by nate on 3/8/19
 */
class User : Principal {

	val id: String
	@get:JsonIgnore
	val dateCreated: Instant?
	private val name: String?
	@get:JsonIgnore
	val role: Role?
	@get:JsonIgnore
	val apiKey: String?

	constructor(id: String, dateCreated: Instant, name: String, role: Role, apiKey: String) {
		this.id = id
		this.dateCreated = dateCreated
		this.name = name
		this.role = role
		this.apiKey = apiKey
	}

	constructor(id: String) {
		this.id = id
		dateCreated = null
		name = null
		role = null
		apiKey = null
	}

	override fun getName(): String? {
		return name
	}

	enum class Role {
		USER, ADMIN
	}
}

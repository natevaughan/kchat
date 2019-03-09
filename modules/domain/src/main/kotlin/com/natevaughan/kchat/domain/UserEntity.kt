package com.natevaughan.kchat.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.natevaughan.kchat.api.User
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * Created by nate on 11/23/17
 */
@Entity
data class UserEntity(
        private val name: String,
        @JsonIgnore private val role: User.Role,
        @JsonIgnore private val apiKey: String
): User {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private var id: Long? = null

    override fun getName(): String = name
	override fun getId(): Long? = id
	override fun getRole(): User.Role = role
	override fun getApiKey(): String = apiKey

}
package com.natevaughan.kchat.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.security.Principal
import javax.persistence.Entity
import javax.persistence.ManyToMany

/**
 * Created by nate on 11/22/17
 */
@Entity
class Message(val text: String, val author: User)

@Entity
class Chat(@ManyToMany val messages: Array<Message>, @ManyToMany val participants: Array<User>)

@Entity
class User(private val name: String, val role: Role, val apiKey: String): Principal {
    override fun getName(): String {
        return name
    }
}

enum class Role { USER, ADMIN }
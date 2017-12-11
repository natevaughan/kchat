package com.natevaughan.hat.user

import com.fasterxml.jackson.annotation.JsonIgnore
import java.security.Principal
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

/**
 * Created by nate on 11/23/17
 */
@Entity
data class User(
        private val name: String,
        @JsonIgnore val role: Role,
        @JsonIgnore val apiKey: String
): Principal {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long? = null

    // necessary for explicit override of Principal.getName()
    override fun getName(): String {
        return name
    }
}

enum class Role { USER, ADMIN }
package com.natevaughan.kchat.model.message.user

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
class User(private val name: String, val role: Role, val apiKey: String): Principal {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long? = null

    override fun getName(): String {
        return name
    }
}

enum class Role { USER, ADMIN }


// required for hibernate criteria queries
@StaticMetamodel(User::class)
object User_ {
    @Volatile
    var id: SingularAttribute<User, Long>? = null
    @Volatile
    var name: SingularAttribute<User, String>? = null
    @Volatile
    var apiKey: SingularAttribute<User, String>? = null
}
package com.natevaughan.kchat.service

import com.natevaughan.kchat.api.Participant
import com.natevaughan.kchat.api.User

/**
 * Created by nate on 3/22/19
 */
interface UserService {

    fun findByApiKey(key: String): User

    fun findById(id: String): User

    fun save(user: User): User

    fun delete(user: User): Boolean

    fun listForSpace(spaceId: String, user: User): Collection<Participant>
}
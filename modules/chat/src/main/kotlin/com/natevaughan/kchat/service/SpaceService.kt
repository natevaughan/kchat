package com.natevaughan.kchat.service

import com.natevaughan.kchat.api.Space
import com.natevaughan.kchat.api.SpaceRepo
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.framework.UnauthorizedException
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by nate on 3/22/19
 */
interface SpaceService {

	fun findById(spaceId: String): Space

	fun addUser(space: Space, user: User, userAlias: String?): Boolean

	fun listForUser(user: User): Collection<Space>

	fun checkAccess(spaceId: String, userId: String): Boolean

	fun create(name: String, user: User): Space

}
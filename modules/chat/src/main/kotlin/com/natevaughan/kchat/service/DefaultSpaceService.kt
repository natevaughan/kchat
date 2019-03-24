package com.natevaughan.kchat.service

import com.natevaughan.kchat.api.ContextualPrivilege
import com.natevaughan.kchat.api.Participant
import com.natevaughan.kchat.api.Space
import com.natevaughan.kchat.api.SpaceRepo
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.framework.UnauthorizedException
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by nate on 3/21/19
 */
@Singleton
class DefaultSpaceService @Inject constructor(val spaceRepo: SpaceRepo): SpaceService {

	override fun findById(spaceId: String): Space {
		return spaceRepo.findById(spaceId)
	}

	override fun addUser(space: Space, user: User, userAlias: String?): Boolean {
		return spaceRepo.addUser(space.id, user.id, userAlias ?: user.name!!, ContextualPrivilege.PARTICIPANT)
	}

	override fun listForUser(user: User): Collection<Space> {
		return spaceRepo.findAllForUser(user)
	}


	override fun checkAccess(spaceId: String, userId: String): Boolean {
		return try {
			spaceRepo.checkAccess(spaceId, userId)
		} catch (_: Exception) {
			false
		}
	}

	override fun create(name: String, user: User): Space {
		if (user.role != User.Role.ADMIN) {
			throw UnauthorizedException("User not authorized to create a space")
		}
		val users = listOf(Participant(user, ContextualPrivilege.CREATOR))
		return spaceRepo.save(Space(
				UUID.randomUUID().toString(),
				Instant.now(),
				name,
				users
		))
	}
}
package com.natevaughan.kchat.service

import com.natevaughan.kchat.api.Invite
import com.natevaughan.kchat.api.InviteRepo
import com.natevaughan.kchat.api.Space
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.framework.BadRequestException
import com.natevaughan.kchat.framework.NotFoundException
import com.natevaughan.kchat.properties.invite
import com.natevaughan.kchat.util.TokenGenerator
import com.natpryce.konfig.Configuration
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by nate on 3/21/19
 */
@Singleton
class DefaultInviteService @Inject constructor(val inviteRepo: InviteRepo, val spaceService: SpaceService, val userService: UserService, val config: Configuration): InviteService {

	override fun redeem(token: String, user: User?, userAlias: String?): User {
		val invite = inviteRepo.findByToken(token)
		val validatedUser = if (user == null) {
			if (userAlias == null) {
				throw BadRequestException("A new user must provide a username")
			}
			userService.save(User(UUID.randomUUID().toString(),
					Instant.now(),
					userAlias,
					User.Role.USER,
					TokenGenerator.generateApiKey()
			))
		} else {
			user
		}
		val success = spaceService.addUser(invite.space, validatedUser, userAlias ?: validatedUser.id)
		if (success) {
			inviteRepo.redeem(invite, validatedUser)
			return validatedUser
		}
		throw NotFoundException("Something went wrong adding a user to a space")
	}

	@Throws(java.lang.Exception::class)
	override fun create(spaceId: String, memo: String, user: User): Invite {
		if (spaceService.checkAccess(spaceId, user.id)) {

			return inviteRepo.save(
					Invite(TokenGenerator.generateInviteToken(),
							Instant.now(),
							Space(spaceId),
							user,
							memo,
							Instant.now().plusMillis(config.get(invite.expiration.millis)), null, null))
		}
		throw NotFoundException("Space $spaceId could not be found")
	}

	override fun listForUser(user: User): List<Invite> {
		return inviteRepo.findBySender(user)
	}
}
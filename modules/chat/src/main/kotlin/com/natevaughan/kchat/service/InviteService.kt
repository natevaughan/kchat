package com.natevaughan.kchat.service

import com.natevaughan.kchat.api.Invite
import com.natevaughan.kchat.api.User

/**
 * Created by nate on 3/22/19
 */
interface InviteService {

	fun redeem(token: String, user: User?, userAlias: String?): User
	fun create(spaceId: String, memo: String, user: User): Invite
	fun listForUser(user: User): List<Invite>
}
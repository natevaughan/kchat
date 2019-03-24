package com.natevaughan.kchat.api

/**
 * Created by nate on 3/20/19
 */
interface InviteRepo : Repository<Invite> {
	fun findByToken(token: String): Invite
	fun findBySender(sender: User): List<Invite>
	fun redeem(invite: Invite, validatedUser: User): Boolean
}

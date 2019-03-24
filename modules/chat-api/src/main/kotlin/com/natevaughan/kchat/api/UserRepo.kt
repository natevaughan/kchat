package com.natevaughan.kchat.api

/**
 * Created by nate on 3/8/19
 */
interface UserRepo : Repository<User> {
	fun findByApiKey(key: String): User
	fun getUsersForSpace(spaceId: String): Collection<Participant>
}

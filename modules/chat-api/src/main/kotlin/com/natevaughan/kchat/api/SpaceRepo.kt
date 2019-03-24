package com.natevaughan.kchat.api

/**
 * Created by nate on 3/20/19
 */
interface SpaceRepo : Repository<Space> {
	fun findAllForUser(user: User): Collection<Space>
	fun addUser(spaceId: String, userId: String, userAlias: String, contextualPrivilege: ContextualPrivilege): Boolean
	fun checkAccess(spaceId: String, userId: String): Boolean
}

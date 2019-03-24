package com.natevaughan.kchat.domain

import com.natevaughan.kchat.api.ConnectionPool
import com.natevaughan.kchat.api.ContextualPrivilege
import com.natevaughan.kchat.api.Participant
import com.natevaughan.kchat.api.Space
import com.natevaughan.kchat.api.SpaceRepo
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.api.UserRepo
import com.natevaughan.kchat.domain.jooq.Tables.SPACE
import com.natevaughan.kchat.domain.jooq.Tables.SPACE_USER
import com.natevaughan.kchat.framework.BadRequestException
import com.natevaughan.kchat.framework.NotFoundException
import org.jooq.Record
import org.jooq.SQLDialect
import org.jooq.exception.DataAccessException
import org.jooq.impl.DSL
import java.sql.SQLException
import java.sql.SQLIntegrityConstraintViolationException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by nate on 3/16/19
 */
@Singleton
class JooqSpaceRepo @Inject
constructor(private val cp: ConnectionPool, private val userRepo: UserRepo) : SpaceRepo {

	override fun checkAccess(spaceId: String, userId: String): Boolean {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val r = create.select()
					.from(SPACE_USER)
					.where(SPACE_USER.USER_ID.eq(getBytes(userId)))
					.and(SPACE_USER.SPACE_ID.eq(getBytes(spaceId)))
					.fetch()

			return r.size > 0

		} finally {
			conn.close()
		}

	}

	override fun findAllForUser(user: User): Collection<Space> {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val r = create.select()
					.from(SPACE)
					.innerJoin(SPACE_USER)
					.on(SPACE.ID.eq(SPACE_USER.SPACE_ID))
					.where(SPACE_USER.USER_ID.eq(getBytes(user.id)))
					.fetch()

			return createFromRecordList(r)

		} finally {
			conn.close()
		}

	}

	override fun addUser(spaceId: String, userId: String, userAlias: String, contextualPrivilege: ContextualPrivilege): Boolean {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val r = create.insertInto(SPACE_USER, SPACE_USER.SPACE_ID, SPACE_USER.USER_ID, SPACE_USER.USER_ALIAS, SPACE_USER.CONTEXTUAL_PRIVILEGE)
					.values(getBytes(spaceId), getBytes(userId), userAlias, contextualPrivilege.level).execute()

			return r > 0

		}  catch (e: DataAccessException) {
			if (e.cause is SQLIntegrityConstraintViolationException) {
				throw BadRequestException("User already has access to space")
			}
			return false
		} finally {
			conn.close()
		}
	}

	override fun findById(id: String): Space {
		val conn = cp.connection
		try {
			val users = userRepo.getUsersForSpace(id)

			val create = DSL.using(conn, SQLDialect.MYSQL)
			val r = create.select()
					.from(SPACE)
					.where(SPACE.ID.eq(getBytes(id)))
					.fetchOne()

			return createFromRecord(r, users)

		} finally {
			conn.close()
		}

	}

	override fun save(entity: Space): Space {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val created = create.insertInto(SPACE, SPACE.ID, SPACE.NAME)
					.values(getBytes(entity.id), entity.name)
					.execute()

			if (created < 1) {
				throw SQLException("Unable to save Space")
			}

			for (user in entity.users) {
				val success = addUser(entity.id, user.user.id, user.user.name!!, user.role)
				if (!success) {
					throw RuntimeException("Unable to add user to space")
				}
			}

			return entity

		} finally {
			conn.close()
		}
	}

	override fun delete(entity: Space): Boolean {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val count = create.delete(SPACE)
					.where(SPACE.ID.eq(getBytes(entity.id)))
					.execute()

			if (count != 1) {
				throw SQLException("Unable to save Invite $entity")
			}

			return true
		} finally {
			conn.close()
		}
	}

	private fun createFromRecordList(r: List<Record>?): List<Space> {
		if (r == null) {
			return emptyList()
		}

		return r.map { createFromRecord(it) }
	}

	private fun createFromRecord(r: Record?, users: Collection<Participant> = emptyList()): Space {
		if (r == null) {
			throw NotFoundException("Space not found")
		}
		return Space(getUuid(r.get(SPACE.ID)).toString(),
				r.get(SPACE.DATE_CREATED).toInstant(),
				r.get(SPACE.NAME),
				users
		)
	}
}

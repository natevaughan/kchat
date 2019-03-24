package com.natevaughan.kchat.domain

import com.natevaughan.kchat.api.ConnectionPool
import com.natevaughan.kchat.api.ContextualPrivilege
import com.natevaughan.kchat.api.Participant
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.api.UserRepo
import com.natevaughan.kchat.domain.jooq.Tables.SPACE_USER
import com.natevaughan.kchat.domain.jooq.Tables.USER
import com.natevaughan.kchat.framework.NotFoundException
import org.jooq.Record
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import java.sql.SQLException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by nate on 3/16/19
 */
@Singleton
class JooqUserRepo @Inject
constructor(private val cp: ConnectionPool) : UserRepo {

	override fun findByApiKey(key: String): User {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val r = create.select().from(USER).where(USER.API_KEY.eq(key)).fetchOne()

			return createFromRecord(r)

		} finally {
			conn.close()
		}
	}

	override fun getUsersForSpace(spaceId: String): Collection<Participant> {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val r = create.select()
					.from(USER)
					.innerJoin(SPACE_USER)
					.on(USER.ID.eq(SPACE_USER.USER_ID))
					.where(SPACE_USER.SPACE_ID.eq(getBytes(spaceId))).fetch()

			return createFromRecordList(r)

		} finally {
			conn.close()
		}
	}

	override fun findById(id: String): User {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val r = create.select().from(USER).where(USER.ID.eq(getBytes(id))).fetchOne()

			return createFromRecord(r)

		} finally {
			conn.close()
		}
	}

	override fun save(entity: User): User {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val count = create.insertInto(USER, USER.ID, USER.NAME, USER.ROLE, USER.API_KEY)
					.values(getBytes(entity.id), entity.name, entity.role!!.ordinal, entity.apiKey)
					.execute()

			if (count != 1) {
				throw SQLException("Unable to save User $entity")
			}

			return entity

		} finally {
			conn.close()
		}
	}

	override fun delete(entity: User): Boolean {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val count = create.delete(USER)
					.where(USER.ID.eq(getBytes(entity.id)))
					.execute()

			if (count != 1) {
				throw SQLException("Unable to save User $entity")
			}

			return true

		} finally {
			conn.close()
		}
	}

	private fun createFromRecordList(r: List<Record>?): List<Participant> {
		if (r == null) {
			return emptyList()
		}

		return r.map { Participant(createFromRecord(it), ContextualPrivilege.byLevel(it.get(SPACE_USER.CONTEXTUAL_PRIVILEGE))!!) }
	}

	private fun createFromRecord(r: Record?): User {
		if (r == null) {
			throw NotFoundException("User not found")
		}
		return User(r.getValue(USER.ID_TEXT),
				r.getValue(USER.DATE_CREATED).toInstant(),
				r.getValue(USER.NAME),
				User.Role.values()[r.getValue(USER.ROLE)],
				r.getValue(USER.API_KEY)
		)
	}
}

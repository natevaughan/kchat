package com.natevaughan.kchat.domain

import com.natevaughan.kchat.api.ConnectionPool
import com.natevaughan.kchat.api.Invite
import com.natevaughan.kchat.api.InviteRepo
import com.natevaughan.kchat.api.Space
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.domain.jooq.Tables.INVITE
import com.natevaughan.kchat.framework.BadRequestException
import com.natevaughan.kchat.framework.NotFoundException
import org.jooq.Record
import org.jooq.SQLDialect
import org.jooq.exception.DataAccessException
import org.jooq.impl.DSL
import java.sql.SQLException
import java.sql.SQLIntegrityConstraintViolationException
import java.sql.Timestamp
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by nate on 3/16/19
 */
@Singleton
class JooqInviteRepo @Inject constructor(private val cp: ConnectionPool) : InviteRepo {

	override fun findByToken(token: String): Invite {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val r = create.select().from(INVITE).where(INVITE.TOKEN.eq(token)).and(INVITE.RECIPIENT_ID.isNull).fetchOne()

			return createFromRecord(r)
		} finally {
			conn.close()
		}
	}

	override fun findBySender(sender: User): List<Invite> {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val r = create.select().from(INVITE).where(INVITE.CREATOR_ID.eq(getBytes(sender.id))).fetch()

			return createFromRecordList(r)

		} finally {
			conn.close()
		}

	}

	override fun findById(token: String): Invite {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val r = create.select().from(INVITE).where(INVITE.TOKEN.eq(token)).fetchOne()

			return createFromRecord(r)

		} finally {
			conn.close()
		}

	}

	override fun save(entity: Invite): Invite {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val count = create.insertInto(INVITE, INVITE.TOKEN, INVITE.CREATOR_ID, INVITE.SPACE_ID, INVITE.MEMO, INVITE.EXPIRES)
					.values(entity.token, getBytes(entity.sender.id), getBytes(entity.space.id), entity.memo, Timestamp.from(entity.expires))
					.execute()

			if (count != 1) {
				throw SQLException("Unable to save Invite $entity")
			}
			return entity

		} finally {
			conn.close()
		}

	}

	override fun redeem(invite: Invite, validatedUser: User): Boolean {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val count = create.update(INVITE)
					.set(INVITE.RECIPIENT_ID, getBytes(validatedUser.id))
					.set(INVITE.DATE_REDEEMED, Timestamp.from(Instant.now()))
					.where(INVITE.TOKEN.eq(invite.token))
					.execute()
			if (count != 1) {
				return false
			}
			return true

		} catch (e: DataAccessException) {
			if (e.cause is SQLIntegrityConstraintViolationException) {
				throw BadRequestException("User already has access to this space")
			}
			return false
		} finally {
			conn.close()
		}
	}

	override fun delete(entity: Invite): Boolean {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val count = create.delete(INVITE)
					.where(INVITE.TOKEN.eq(entity.token))
					.execute()
			if (count != 1) {
				return false
			}
			return true

		} finally {
			conn.close()
		}
	}

	private fun createFromRecordList(r: List<Record>?): List<Invite> {
		if (r == null) {
			return emptyList()
		}

		return r.map { createFromRecord(it) }
	}

	private fun createFromRecord(r: Record?): Invite {

		if (r == null) {
			throw NotFoundException("Invite was not found")
		}

		val recipient = if (r.get(INVITE.RECIPIENT_ID_TEXT) != null) {
			User(r.get(INVITE.RECIPIENT_ID_TEXT))
		} else { null }

		val dateRedeemed = if (r.get(INVITE.DATE_REDEEMED) != null) {
			r.get(INVITE.DATE_REDEEMED).toInstant()
		} else { null }

		return Invite(
				r.get(INVITE.TOKEN),
				r.get(INVITE.DATE_CREATED).toInstant(),
				Space(r.get(INVITE.SPACE_ID_TEXT)),
				User(r.get(INVITE.CREATOR_ID_TEXT)),
				r.get(INVITE.MEMO),
				r.get(INVITE.EXPIRES).toInstant(),
				recipient,
				dateRedeemed
		)

	}
}

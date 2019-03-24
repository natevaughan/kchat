package com.natevaughan.kchat.domain

import com.natevaughan.kchat.api.Chat
import com.natevaughan.kchat.api.ChatRepo
import com.natevaughan.kchat.api.ConnectionPool
import com.natevaughan.kchat.api.ContextualPrivilege
import com.natevaughan.kchat.api.Space
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.domain.jooq.Tables.CHAT
import com.natevaughan.kchat.domain.jooq.Tables.CHAT_USER
import com.natevaughan.kchat.framework.BadRequestException
import com.natevaughan.kchat.framework.NotFoundException
import org.jooq.Record
import org.jooq.Result
import org.jooq.SQLDialect
import org.jooq.exception.DataAccessException
import org.jooq.impl.DSL
import java.sql.SQLException
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by nate on 3/16/19
 */
@Singleton
class JooqChatRepo @Inject constructor(private val cp: ConnectionPool) : ChatRepo {

	override fun listForUserAndSpace(spaceId: String, user: User): Collection<Chat> {
		val conn = cp.connection
		try {
			val chatList = HashSet<Chat>()

			val create = DSL.using(conn, SQLDialect.MYSQL)
			var result: Result<Record>? = create.select().from(CHAT)
					.where(CHAT.CREATOR_ID.eq(getBytes(user.id)))
					.and(CHAT.SPACE_ID.eq(getBytes(spaceId)))
					.fetch()

			if (result != null) {
				chatList.addAll(result.map { parseFromRecord(it) })
			}

			result = create.select()
					.from(CHAT)
					.innerJoin(CHAT_USER)
					.on(CHAT_USER.CHAT_ID.eq(CHAT.ID))
					.where(CHAT_USER.USER_ID.eq(getBytes(user.id)))
					.and(CHAT.SPACE_ID.eq(getBytes(spaceId)))
					.fetch()

			if (result != null) {
				chatList.addAll(result.map { parseFromRecord(it) })
			}

			result = create.select()
					.from(CHAT)
					.where(CHAT.TYPE.eq(0))
					.and(CHAT.SPACE_ID.eq(getBytes(spaceId)))
					.fetch()

			if (result != null) {
				chatList.addAll(result.map { parseFromRecord(it) })
			}

			return chatList
		} finally {
			conn.close()
		}
	}

	override fun findById(id: String): Chat {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val r = create.select().from(CHAT).where(CHAT.ID.eq(getBytes(id))).fetchOne()

			return parseFromRecord(r)

		} finally {
			conn.close()
		}
	}

	override fun addParticipant(chatId: String, userId: String): Boolean {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val count = create.insertInto(CHAT_USER, CHAT_USER.CHAT_ID, CHAT_USER.USER_ID)
					.values(getBytes(chatId), getBytes(userId))
					.execute()

			if (count != 1) {
				throw SQLException("Unable to add user to chat $userId, $chatId")
			}

			return true

		} catch (e: DataAccessException) {
			throw BadRequestException("Invalid user ID or participant already exists in chat: ${userId}")
		} finally {
			conn.close()
		}
	}

	override fun save(entity: Chat): Chat {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			create.transaction({ configuration ->
				val tx = DSL.using(configuration)
				val count = tx.insertInto(CHAT, CHAT.ID, CHAT.NAME, CHAT.SPACE_ID, CHAT.TYPE)
						.values(getBytes(entity.id), entity.name, getBytes(entity.space!!.id), entity.type!!.ordinal)
						.execute()

				if (count != 1) {
					throw SQLException("Unable to save Chat $entity")
				}

				var insert = tx.insertInto(CHAT_USER, CHAT_USER.CHAT_ID, CHAT_USER.USER_ID)

				for (participant in entity.participants) {
					insert = insert.values(getBytes(entity.id), getBytes(participant.user.id))
				}
				insert.execute()

			})

			return entity

		} catch (e: DataAccessException) {
			throw BadRequestException("Invalid space ID or chat already exists in space name ${entity.name}")
		} finally {
			conn.close()
		}
	}

	override fun getPrivileges(chatId: String, userId: String): ContextualPrivilege? {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val result = create.select()
					.from(CHAT_USER)
					.where(CHAT_USER.USER_ID.eq(getBytes(userId)))
					.and(CHAT_USER.CHAT_ID.eq(getBytes(chatId)))
					.fetchOne() ?: return null

			return ContextualPrivilege.byLevel(result.get(CHAT_USER.CONTEXTUAL_PRIVILEGE))
		} catch (e: DataAccessException) {
			throw BadRequestException("Invalid space ID or chat already exists in space name")
		} finally {
			conn.close()
		}
	}

	override fun findNewForUser(user: User, timestamp: Long?): Collection<Chat> {
		return emptyList()
	}

	override fun delete(entity: Chat): Boolean {
		return delete(entity.id)
	}

	override fun delete(chatId: String): Boolean {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val count = create.delete(CHAT)
					.where(CHAT.ID.eq(getBytes(chatId)))
					.execute()

			if (count != 1) {
				throw SQLException("Unable to delete chat $chatId")
			}

			return true
		} finally {
			conn.close()
		}
	}

	private fun parseFromRecord(r: Record?): Chat {
		if (r == null) {
			throw NotFoundException("Chat not found")
		}
		return Chat(r.get(CHAT.ID_TEXT),
				r.get(CHAT.DATE_CREATED).toInstant(),
				r.get(CHAT.NAME),
				Space(r.get(CHAT.SPACE_ID_TEXT)),
				Chat.Type.values()[r.get(CHAT.TYPE)],
				ArrayList()
		)
	}
}

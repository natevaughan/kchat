package com.natevaughan.kchat.domain

import com.natevaughan.kchat.api.Chat
import com.natevaughan.kchat.api.ConnectionPool
import com.natevaughan.kchat.api.Message
import com.natevaughan.kchat.api.MessageRepo
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.domain.jooq.Tables.MESSAGE
import com.natevaughan.kchat.framework.NotFoundException
import org.jooq.Record
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import java.sql.SQLException
import java.sql.Timestamp
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by nate on 3/16/19
 */
@Singleton
class JooqMessageRepo @Inject constructor(private val cp: ConnectionPool) : MessageRepo {

	override fun findForChatSinceTimestamp(chat: Chat, timestamp: Instant): Iterable<Message> {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val result = create.select()
					.from(MESSAGE)
					.where(MESSAGE.DATE_CREATED.gt(Timestamp.from(timestamp)))
					.orderBy(MESSAGE.DATE_CREATED.asc()).fetch() ?: return emptyList()

			return createFromRecordList(result)

		} finally {
			conn.close()
		}
	}

	override fun findRecent(chat: Chat, count: Int): Iterable<Message> {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			println(create.select().from(MESSAGE)
					.where(MESSAGE.CHAT_ID.eq(getBytes(chat.id)))
					.orderBy(MESSAGE.DATE_CREATED.asc())
					.limit(count).getSQL())
			val r = create.select().from(MESSAGE)
					.where(MESSAGE.CHAT_ID.eq(getBytes(chat.id)))
					.orderBy(MESSAGE.DATE_CREATED.asc())
					.limit(count).fetch() ?: return emptyList()

			return createFromRecordList(r)

		} finally {
			conn.close()
		}
	}

	override fun findById(id: String): Message {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val r = create.select().from(MESSAGE).where(MESSAGE.ID.eq(getBytes(id))).fetchOne()

			return createFromRecord(r)

		} finally {
			conn.close()
		}
	}

	override fun save(entity: Message): Message {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val count = create.insertInto(MESSAGE, MESSAGE.ID, MESSAGE.TEXT, MESSAGE.AUTHOR_ID, MESSAGE.CHAT_ID)
					.values(getBytes(entity.id), entity.text, getBytes(entity.author.id), getBytes(entity.chat.id))
					.execute()


			if (count != 1) {
				throw SQLException("Unable to save Message $entity")
			}

			return entity

		} finally {
			conn.close()
		}
	}

	override fun delete(entity: Message): Boolean {
		val conn = cp.connection
		try {
			val create = DSL.using(conn, SQLDialect.MYSQL)
			val count = create.delete(MESSAGE)
					.where(MESSAGE.ID.eq(getBytes(entity.id)))
					.execute()

			if (count != 1) {
				throw SQLException("Unable to delete Message $entity")
			}

			return true
		} finally {
			conn.close()
		}
	}

	private fun createFromRecordList(r: List<Record>?): List<Message> {
		if (r == null) {
			return emptyList()
		}

		return r.map { createFromRecord(it) }
	}

	private fun createFromRecord(r: Record?): Message {
		if (r == null) {
			throw NotFoundException("Message not found")
		}
		return Message(r.getValue(MESSAGE.ID_TEXT),
				r.getValue(MESSAGE.DATE_CREATED).toInstant(),
				r.getValue(MESSAGE.TEXT),
				User(r.getValue(MESSAGE.AUTHOR_ID_TEXT)),
				Chat(r.getValue(MESSAGE.CHAT_ID_TEXT)),
				r.getValue(MESSAGE.LAST_EDITED).toInstant()
		)
	}
}

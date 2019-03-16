package com.natevaughan.kchat.domain;

import com.natevaughan.kchat.api.Chat;
import com.natevaughan.kchat.api.ConnectionPool;
import com.natevaughan.kchat.api.Message;
import com.natevaughan.kchat.api.MessageRepo;
import com.natevaughan.kchat.api.User;
import com.natevaughan.kchat.domain.jooq.tables.records.MessageRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;

import static com.natevaughan.kchat.domain.jooq.Tables.MESSAGE;

/**
 * Created by nate on 3/16/19
 */
public class JooqMessageRepo implements MessageRepo {

	private final ConnectionPool cp;

	@Inject
	public JooqMessageRepo(ConnectionPool cp) {
		this.cp = cp;
	}

	@Override
	public Iterable<Message> findForChatSinceTimestamp(Chat chat, Instant timestamp) {
		return null;
	}

	@Override
	public Iterable<Message> findRecent(Chat chat, Integer count) {
		return null;
	}

	@Override
	public Message findById(Long id) {
		try (Connection conn = cp.getConnection()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			Record r = create.select().from(MESSAGE).where(MESSAGE.ID.eq(id)).fetchOne();

			if (r == null) {
				return null;
			}

			return new Message(r.getValue(MESSAGE.ID),
					r.getValue(MESSAGE.DATE_CREATED).toInstant(),
					r.getValue(MESSAGE.TEXT),
					new User(r.getValue(MESSAGE.AUTHOR_ID), null, null, null, null),
					new Chat(r.getValue(MESSAGE.CHAT_ID), null, null, null, null),
					r.getValue(MESSAGE.LAST_EDITED).toInstant()
			);

		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to MySQL", e);
		}
	}

	@Override
	public Message save(Message entity) {
		try (Connection conn = cp.getConnection()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			MessageRecord r = create.insertInto(MESSAGE, MESSAGE.TEXT, MESSAGE.AUTHOR_ID, MESSAGE.CHAT_ID).values(entity.getText(), entity.getAuthor().getId(), entity.getChat().getId()).returning(MESSAGE.ID, MESSAGE.DATE_CREATED, MESSAGE.LAST_EDITED)
					.fetchOne();

			if (r == null) {
				return null;
			}

			return new Message(r.getId(),
					r.getDateCreated().toInstant(),
					entity.getText(),
					entity.getAuthor(),
					entity.getChat(),
					r.getLastEdited().toInstant()
			);
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to MySQL", e);
		}
	}

	@Override
	public Boolean delete(Message entity) {
		try (Connection conn = cp.getConnection()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			create.delete(MESSAGE)
					.where(MESSAGE.ID.eq(entity.getId()))
					.execute();
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to MySQL", e);
		}
		return true;
	}
}

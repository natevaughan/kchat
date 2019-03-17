package com.natevaughan.kchat.domain;

import com.natevaughan.kchat.api.Chat;
import com.natevaughan.kchat.api.ChatRepo;
import com.natevaughan.kchat.api.ConnectionPool;
import com.natevaughan.kchat.api.User;
import com.natevaughan.kchat.domain.jooq.tables.records.ChatRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.natevaughan.kchat.domain.jooq.Tables.CHAT;
import static com.natevaughan.kchat.domain.jooq.Tables.CHAT_USER;

/**
 * Created by nate on 3/16/19
 */
public class JooqChatRepo implements ChatRepo {

	private final ConnectionPool cp;

	@Inject
	public JooqChatRepo(ConnectionPool cp) {
		this.cp = cp;
	}

	@Override
	public Chat findByKey(String key) {
		try (Connection conn = cp.getConnection()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			Record r = create.select().from(CHAT).where(CHAT.ACCESS_KEY.eq(key)).fetchOne();

			if (r == null) {
				return null;
			}

			return new Chat(r.get(CHAT.ID),
					r.get(CHAT.DATE_CREATED).toInstant(),
					r.get(CHAT.NAME),
					r.get(CHAT.ACCESS_KEY),
					new User(r.get(CHAT.CREATOR_ID), null, null, null, null),
					null
			);
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to MySQL", e);
		}
	}

	@Override
	public Collection<Chat> findNewForUser(User user, Long timestamp) {
		return null;
	}

	@Override
	public Collection<Chat> listForUser(User user) {
		try (Connection conn = cp.getConnection()) {
			List<Chat> chatList = new ArrayList<>();

			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			Result<Record> result = create.select().from(CHAT).where(CHAT.CREATOR_ID.eq(user.getId())).fetch();

			if (result == null) {
				return chatList;
			}

			for (Record r : result ) {
				chatList.add(new Chat(r.get(CHAT.ID),
						r.get(CHAT.DATE_CREATED).toInstant(),
						r.get(CHAT.NAME),
						r.get(CHAT.ACCESS_KEY),
						new User(r.get(CHAT.CREATOR_ID), null, null, null, null),
						null
				));
			}

			result = create.select()
					.from(CHAT)
					.innerJoin(CHAT_USER)
					.on(CHAT_USER.CHAT_ID.eq(CHAT.ID))
					.where(CHAT_USER.PARTICIPANTS_ID.eq(user.getId()))
					.fetch();

			if (result == null) {
				return chatList;
			}

			for (Record r : result ) {
				chatList.add(new Chat(r.get(CHAT.ID),
						r.get(CHAT.DATE_CREATED).toInstant(),
						r.get(CHAT.NAME),
						r.get(CHAT.ACCESS_KEY),
						new User(r.get(CHAT.CREATOR_ID), null, null, null, null),
						null
				));
			}

			return chatList;
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to MySQL", e);
		}
	}

	@Override
	public Chat findById(Long id) {
		try (Connection conn = cp.getConnection()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			Record r = create.select().from(CHAT).where(CHAT.ID.eq(id)).fetchOne();

			if (r == null) {
				return null;
			}

			return new Chat(r.get(CHAT.ID),
					r.get(CHAT.DATE_CREATED).toInstant(),
					r.get(CHAT.NAME),
					r.get(CHAT.ACCESS_KEY),
					new User(r.get(CHAT.CREATOR_ID), null, null, null, null),
					null
			);
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to MySQL", e);
		}
	}

	@Override
	public Chat save(Chat entity) {
		try (Connection conn = cp.getConnection()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			ChatRecord r = create.insertInto(CHAT, CHAT.NAME, CHAT.CREATOR_ID, CHAT.ACCESS_KEY)
					.values(entity.getName(), entity.getCreator().getId(), entity.getAccessKey())
					.returning(CHAT.ID, CHAT.DATE_CREATED)
					.fetchOne();

			if (r == null) {
				return null;
			}

			for (User participant : entity.getParticipants()) {
				create.insertInto(CHAT_USER, CHAT_USER.CHAT_ID, CHAT_USER.PARTICIPANTS_ID)
						.values(r.getId(), participant.getId());
			}

			return new Chat(r.getId(),
					r.getDateCreated().toInstant(),
					entity.getName(),
					entity.getAccessKey(),
					entity.getCreator(),
					entity.getParticipants()
			);
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to MySQL", e);
		}
	}

	@Override
	public Boolean delete(Chat entity) {
		try (Connection conn = cp.getConnection()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			create.delete(CHAT)
					.where(CHAT.ID.eq(entity.getId()))
					.execute();
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to MySQL", e);
		}
		return true;
	}
}

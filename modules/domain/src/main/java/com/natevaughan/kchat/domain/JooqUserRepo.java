package com.natevaughan.kchat.domain;

import com.natevaughan.kchat.api.ConnectionPool;
import com.natevaughan.kchat.api.User;
import com.natevaughan.kchat.api.UserRepo;
import com.natevaughan.kchat.domain.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;

import static com.natevaughan.kchat.domain.jooq.Tables.USER;

/**
 * Created by nate on 3/16/19
 */
public class JooqUserRepo implements UserRepo {

	private final ConnectionPool cp;

	@Inject
	public JooqUserRepo(ConnectionPool cp) {
		this.cp = cp;
	}

	@Override
	public User findByApiKey(String key) {
		try (Connection conn = cp.getConnection()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			Record r = create.select().from(USER).where(USER.API_KEY.eq(key)).fetchOne();

			if (r == null) {
				return null;
			}

			return new User(r.getValue(USER.ID),
					r.getValue(USER.DATE_CREATED).toInstant(),
					r.getValue(USER.NAME),
					User.Role.values()[r.getValue(USER.ROLE)],
					r.getValue(USER.API_KEY)
			);
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to MySQL", e);
		}
	}

	@Override
	public User findById(Long id) {
		try (Connection conn = cp.getConnection()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			Record r = create.select().from(USER).where(USER.ID.eq(id)).fetchOne();

			if (r == null) {
				return null;
			}

			return new User(r.getValue(USER.ID),
					r.getValue(USER.DATE_CREATED).toInstant(),
					r.getValue(USER.NAME),
					User.Role.values()[r.getValue(USER.ROLE)],
					r.getValue(USER.API_KEY)
			);
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to MySQL", e);
		}
	}

	@Override
	public User save(User entity) {
		try (Connection conn = cp.getConnection()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			UserRecord r = create.insertInto(USER, USER.NAME, USER.ROLE, USER.API_KEY).values(entity.getName(), entity.getRole().ordinal(), entity.getApiKey()).returning(USER.ID, USER.DATE_CREATED)
					.fetchOne();

			if (r == null) {
				return null;
			}

			return new User(r.getId(),
					r.getDateCreated().toInstant(),
					entity.getName(),
					entity.getRole(),
					entity.getApiKey()
			);
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to MySQL", e);
		}
	}

	@Override
	public Boolean delete(User entity) {
		try (Connection conn = cp.getConnection()) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			create.delete(USER)
					.where(USER.ID.eq(entity.getId()))
					.execute();
		} catch (SQLException e) {
			throw new RuntimeException("Could not connect to MySQL", e);
		}
		return true;
	}
}

package com.natevaughan.kchat.domain;
// For convenience, always static import your generated tables and jOOQ functions to decrease verbosity:
import com.natevaughan.kchat.api.ConnectionPool;
import com.natevaughan.kchat.api.User;
import com.natevaughan.kchat.api.UserRepo;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import static com.natevaughan.kchat.domain.jooq.Tables.*;
import java.sql.*;

/**
 * Created by nate on 3/16/19
 */

public class JooqDemo {
	public static void main(String[] args) {
		String userName = "root";
		String password = "";
		String url = "jdbc:mysql://localhost:3306/kchat";
		ConnectionPool connectionPool = new HikariConnectionPool(url, userName, password);
		UserRepo userRepo = new JooqUserRepo(connectionPool);
		User user = userRepo.findById(1L);

		System.out.println("ID: " + user.getId() + " name: " + user.getName() );
	}
}

package com.natevaughan.kchat.domain;

import com.natevaughan.kchat.api.ConnectionPool;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by nate on 3/16/19
 */
public class HikariConnectionPool implements ConnectionPool {

	private final HikariDataSource ds;

	public HikariConnectionPool(String jdbcUrl, String user, String password) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbcUrl);
		config.setUsername(user);
		config.setPassword(password);
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		ds = new HikariDataSource(config);
	}

	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
}
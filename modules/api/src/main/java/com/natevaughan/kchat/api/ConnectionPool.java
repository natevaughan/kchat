package com.natevaughan.kchat.api;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by nate on 3/16/19
 */
public interface ConnectionPool {
	Connection getConnection() throws SQLException;
}

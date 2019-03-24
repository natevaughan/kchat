package com.natevaughan.kchat.framework

import com.natevaughan.kchat.api.ConnectionPool
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.sql.SQLException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by nate on 3/16/19
 */
@Singleton
class HikariConnectionPool @Inject constructor(val ds: HikariDataSource) : ConnectionPool {

	override val connection: Connection
		@Throws(SQLException::class)
		get() = ds.connection

	override fun close() {
		ds.close()
	}
}
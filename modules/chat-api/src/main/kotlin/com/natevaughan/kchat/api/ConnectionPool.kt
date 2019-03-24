package com.natevaughan.kchat.api

import java.sql.Connection

/**
 * Created by nate on 3/16/19
 */
interface ConnectionPool {
	val connection: Connection
	fun close()
}


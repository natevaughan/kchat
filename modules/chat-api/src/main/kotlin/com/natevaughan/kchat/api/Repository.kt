package com.natevaughan.kchat.api

/**
 * Created by nate on 3/8/19
 */
interface Repository<T> {
	fun findById(id: String): T
	fun save(entity: T): T
	fun delete(entity: T): Boolean
}

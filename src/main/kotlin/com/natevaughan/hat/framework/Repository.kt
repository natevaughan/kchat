package com.natevaughan.hat.framework

/**
 * Created by nate on 11/23/17
 */
interface Repository<T> {
    fun findById(id: Long): T?
    fun save(entity: T): T
    fun delete(entity: T): Boolean
}


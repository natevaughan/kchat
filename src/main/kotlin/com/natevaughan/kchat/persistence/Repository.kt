package com.natevaughan.kchat.persistence

/**
 * Created by nate on 11/23/17
 */

interface Identifieable<T> {
    fun findById(id: Long): T
    fun save(entity: T): T

}


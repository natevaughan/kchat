package com.natevaughan.hat

import com.natevaughan.hat.user.User
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne

/**
 * Created by nate on 12/9/17
 */
@Entity
class Hat(
        val name: String,
        val accessKey: String,
        @ManyToOne val creator: User,
        @ManyToMany val participants: Collection<User> = setOf()
): Comparable<Hat> {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    val id: Long? = null

    override fun compareTo(other: Hat): Int {
        return this.name.compareTo(other.name)
    }
}

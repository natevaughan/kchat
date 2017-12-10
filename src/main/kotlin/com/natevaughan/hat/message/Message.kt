package com.natevaughan.hat.message

import com.natevaughan.hat.user.User
import javax.persistence.*
import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

/**
 * Created by nate on 11/23/17
 */
@Entity
data class Message(val text: String, val timestamp: Long, @ManyToOne val author: User, val lastEdited: Long? = null): Comparable<Message> {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long? = null

    override fun compareTo(other: Message): Int {
        return (timestamp - other.timestamp).toInt()
    }
}
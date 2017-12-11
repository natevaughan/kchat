package com.natevaughan.hat.message

import com.fasterxml.jackson.annotation.JsonIgnore
import com.natevaughan.hat.Hat
import com.natevaughan.hat.user.User
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

/**
 * Created by nate on 11/23/17
 */

@Entity
data class Message(
    val text: String,
    val timestamp: Long,
    @ManyToOne val author: User,
    @ManyToOne @JsonIgnore val hat: Hat,
    val lastEdited: Long? = null
): Comparable<Message> {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long? = null

    override fun compareTo(other: Message): Int {
        return (timestamp - other.timestamp).toInt()
    }
}
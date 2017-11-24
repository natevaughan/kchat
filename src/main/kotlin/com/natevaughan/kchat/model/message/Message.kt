package com.natevaughan.kchat.model.message

import com.natevaughan.kchat.model.message.user.User
import javax.persistence.*
import javax.persistence.metamodel.SingularAttribute
import javax.persistence.metamodel.StaticMetamodel

/**
 * Created by nate on 11/23/17
 */

@Entity
data class Message(val text: String, val timestamp: Long, @ManyToOne val author: User) {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    var id: Long? = null
}

// required for hibernate criteria queries
@StaticMetamodel(Message::class)
object Message_ {
    @Volatile
    var id: SingularAttribute<Message, Long>? = null
    @Volatile
    var author: SingularAttribute<Message, User>? = null
    @Volatile
    var timestamp: SingularAttribute<Message, Long>? = null
}
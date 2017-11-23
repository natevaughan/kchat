package com.natevaughan.kchat.model.message

import com.natevaughan.kchat.model.message.user.User
import javax.persistence.*

/**
 * Created by nate on 11/23/17
 */

@Entity
data class Message(val text: String, @ManyToOne val author: User){
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    val id: Long? = null
}
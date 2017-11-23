package com.natevaughan.kchat.model

import com.natevaughan.kchat.model.message.Message
import com.natevaughan.kchat.model.message.user.User

/**
 * Created by nate on 11/22/17
 */

//@Entity
class Chat(val name: String, val messages: Array<Message>, val creator: User){
//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
    val id: Long? = null
}


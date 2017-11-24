package com.natevaughan.kchat.model.chat
/**

import com.natevaughan.kchat.model.message.Message
import com.natevaughan.kchat.model.message.user.User
import javax.persistence.*

 * Created by nate on 11/22/17

@Entity
class Chat(val name: String, @OneToMany val messages: MutableCollection<Message>, @ManyToOne val creator: User, @ManyToMany val participants: List<User>){
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    val id: Long? = null
}

 */

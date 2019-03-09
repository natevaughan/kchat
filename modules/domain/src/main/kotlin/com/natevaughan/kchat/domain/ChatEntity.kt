package com.natevaughan.kchat.domain

import com.natevaughan.kchat.api.Chat
import com.natevaughan.kchat.api.User
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
class ChatEntity(
        private val name: String,
        private val accessKey: String,
        @ManyToOne private val creator: UserEntity,
        @ManyToMany private val participants: Collection<UserEntity> = setOf()
): Chat, Comparable<ChatEntity> {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private val id: Long? = null

    override fun compareTo(other: ChatEntity): Int {
        return this.name.compareTo(other.name)
    }

    override fun getId(): Long? = id
    override fun getName(): String = name
    override fun getAccessKey(): String = accessKey
    override fun getCreator(): User = creator as User
    override fun getParticipants(): Collection<User> = participants

}

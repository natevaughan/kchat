package com.natevaughan.kchat.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.natevaughan.kchat.api.Chat
import com.natevaughan.kchat.api.Message
import com.natevaughan.kchat.api.User
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

/**
 * Created by nate on 11/23/17
 */

@Entity
data class MessageEntity(
    private val text: String,
    private val timestamp: Long,
    @ManyToOne private val author: UserEntity,
    @ManyToOne @JsonIgnore private val chat: ChatEntity,
    private val lastEdited: Long? = null
): Message, Comparable<MessageEntity> {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private var id: Long? = null

    override fun compareTo(other: MessageEntity): Int {
        return (timestamp - other.timestamp).toInt()
    }

	override fun getId(): Long? = id
	override fun getText(): String = text
	override fun getTimestamp(): Long = timestamp
	override fun getLastEdited(): Long? = lastEdited
	override fun getAuthor(): User = author
	override fun getChat(): Chat = chat
	override fun copy(text: String?, lastEdited: Long?): Message {
		return this.copy(text = text, lastEdited = lastEdited)
	}
}
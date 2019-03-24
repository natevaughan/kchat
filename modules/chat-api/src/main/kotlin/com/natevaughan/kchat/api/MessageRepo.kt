package com.natevaughan.kchat.api

import java.time.Instant

/**
 * Created by nate on 3/8/19
 */
interface MessageRepo : Repository<Message> {
	fun findForChatSinceTimestamp(chat: Chat, timestamp: Instant): Iterable<Message>
	fun findRecent(chat: Chat, count: Int): Iterable<Message>
}

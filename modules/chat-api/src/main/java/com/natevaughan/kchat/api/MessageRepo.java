package com.natevaughan.kchat.api;

import java.time.Instant;

/**
 * Created by nate on 3/8/19
 */
public interface MessageRepo extends Repository<Message> {
	Iterable<Message> findForChatSinceTimestamp(Chat chat, Instant timestamp);
	Iterable<Message> findRecent(Chat chat, Integer count);
}

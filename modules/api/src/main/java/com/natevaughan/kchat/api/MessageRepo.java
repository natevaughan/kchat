package com.natevaughan.kchat.api;

/**
 * Created by nate on 3/8/19
 */
public interface MessageRepo extends Repository<Message> {
	Iterable<Message> findForHatSinceTimestamp(Chat chat, Long timestamp);
	Iterable<Message> findRecent(Chat chat, Integer count);
}

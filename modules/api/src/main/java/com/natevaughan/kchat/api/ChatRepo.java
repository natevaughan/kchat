package com.natevaughan.kchat.api;

import java.util.Collection;

/**
 * Created by nate on 12/9/17
 */
public interface ChatRepo extends Repository<Chat> {
	Chat findByKey(String key);
	Collection<Chat> findNewForUser(User user, Long timestamp);
    Collection<Chat> listForUser(User user);
}
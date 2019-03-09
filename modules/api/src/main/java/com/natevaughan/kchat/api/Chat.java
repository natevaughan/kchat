package com.natevaughan.kchat.api;

import java.util.Collection;

/**
 * Created by nate on 3/8/19
 */
public interface Chat {
	Long getId();

	String getName();

	String getAccessKey();

	User getCreator();

	Collection<User> getParticipants();
}

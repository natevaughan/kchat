package com.natevaughan.kchat.api;

/**
 * Created by nate on 3/8/19
 */
public interface Message {
	Long getId();
	String getText();
	Long getTimestamp();
	User getAuthor();
	Chat getChat();
	Long getLastEdited();
	Message copy(String text, Long lastEdited);
}

package com.natevaughan.kchat.domain;

import com.natevaughan.kchat.api.Chat;
import com.natevaughan.kchat.api.ChatRepo;
import com.natevaughan.kchat.api.ConnectionPool;
import com.natevaughan.kchat.api.User;

import javax.inject.Inject;
import java.util.Collection;

/**
 * Created by nate on 3/16/19
 */
public class JooqChatRepo implements ChatRepo {

	private final ConnectionPool cp;

	@Inject
	public JooqChatRepo(ConnectionPool cp) {
		this.cp = cp;
	}

	@Override
	public Chat findByKey(String key) {
		return null;
	}

	@Override
	public Collection<Chat> findNewForUser(User user, Long timestamp) {
		return null;
	}

	@Override
	public Collection<Chat> listForUser(User user) {
		return null;
	}

	@Override
	public Chat findById(Long id) {
		return null;
	}

	@Override
	public Chat save(Chat entity) {
		return null;
	}

	@Override
	public Boolean delete(Chat entity) {
		return null;
	}
}

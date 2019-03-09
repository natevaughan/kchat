package com.natevaughan.kchat.api;

import java.util.Collection;

import static org.junit.Assert.assertNotNull;

public class ChatRepoTest {

	public void testInterfaceMethods() {
		ChatRepo chatRepo = new ChatRepo() {

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
		};
		assertNotNull(chatRepo);
	}
}
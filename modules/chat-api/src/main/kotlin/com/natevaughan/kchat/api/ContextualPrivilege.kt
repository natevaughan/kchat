package com.natevaughan.kchat.api

/**
 * Created by nate on 3/24/19
 *
 * User role in chat or space using integer level to allow future flexibility to add more granular
 * control
 */
enum class ContextualPrivilege(val level: Int) {
	PARTICIPANT(10),
	ADMIN(20),
	CREATOR(30);

	companion object {
		private val mappedByLevel = HashMap<Int, ContextualPrivilege>()

		init {
			values().forEach { mappedByLevel.put(it.level, it) }
		}

		fun byLevel(level: Int): ContextualPrivilege? {
			return mappedByLevel.get(level)
		}
	}
}
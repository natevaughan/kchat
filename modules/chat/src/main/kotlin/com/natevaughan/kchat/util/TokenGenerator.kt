package com.natevaughan.kchat.util

/**
 * WARNING: this token generator has a 1 in 10,578,455,953,408 chance of collision
 */
object TokenGenerator {
	val chars = "BCDFGHJKLMNPQRSTVWXZ123456789"
	val inviteDashPoints = setOf(3, 6)
	val apiKeyDashPoints = setOf(5, 10)

	fun generateApiKey(): String {
		val sb = StringBuilder()
		for (i in 0..14) {
			if (i in apiKeyDashPoints) {
				sb.append('-')
			}
			val idx = (Math.random() * chars.length).toInt()
			sb.append(chars.get(idx))
		}
		return sb.toString()
	}

	fun generateInviteToken(): String {
		val sb = StringBuilder()
		for (i in 0..8) {
			if (i in inviteDashPoints) {
				sb.append('-')
			}
			val idx = (Math.random() * chars.length).toInt()
			sb.append(chars.get(idx))
		}
		return sb.toString()
	}
}

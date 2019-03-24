package com.natevaughan.kchat.util

/**
 * Created by nate on 3/21/19
 */
object ExceptionMessageTranslator {

	/**
	 * Obviously a stupid, fragile, dirty hack (also cheap)
	 */
	fun missingParam(message: String?): String {
		if (message == null) {
			return "Missing JSON property"
		}
		val parts = message.split("[\"")
		val endParts = parts.last().split("\"]")
		return "Missing JSON property: '${endParts.first()}'"
	}
}
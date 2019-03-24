package com.natevaughan.kchat.service

import com.natevaughan.kchat.util.ExceptionMessageTranslator.missingParam
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by nate on 3/21/19
 */
class ExceptionMessageTranslatorTest {

	val testParams = mapOf(
			"Missing JSON property: 'token'" to "Instantiation of [simple type, class com.natevaughan.kchat.rest.Token] value failed for JSON property token due to missing (therefore NULL) value for creator parameter token which is a non-nullable type\n at [Source: (org.glassfish.jersey.message.internal.ReaderInterceptorExecutor\$UnCloseableInputStream); line: 1, column: 23] (through reference chain: com.natevaughan.kchat.rest.Token[\"token\"])",
			"Missing JSON property" to null
	)

	@Test
	fun itShouldTranslateAMessage() {
		for (entry in testParams) {
			assertEquals("The JSON Translator should point out missing properties", entry.key, missingParam(entry.value))
		}
	}
}
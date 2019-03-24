package com.natevaughan.kchat.domain

import java.nio.ByteBuffer
import java.util.UUID

/**
 * source https://stackoverflow.com/questions/24408984/convert-bytearray-to-uuid-java
 */
fun getBytes(uuid: UUID): ByteArray {
	val bb = ByteBuffer.wrap(ByteArray(16))
	bb.putLong(uuid.mostSignificantBits)
	bb.putLong(uuid.leastSignificantBits)

	return bb.array()
}

fun getBytes(uuid: String): ByteArray {
	return getBytes(UUID.fromString(uuid))
}

fun getUuid(bytes: ByteArray): UUID {
	val bb = ByteBuffer.wrap(bytes)
	val high = bb.long
	val low = bb.long
	return UUID(high, low)
}

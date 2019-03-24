package com.natevaughan.kchat.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * source https://www.reddit.com/r/Kotlin/comments/8gbiul/slf4j_loggers_in_3_ways/
 */
inline fun <reified T> logger(from: T): Logger {
	return LoggerFactory.getLogger(T::class.java)
}
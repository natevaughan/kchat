package com.natevaughan.kchat.framework

/**
 * Created by nate on 11/22/17
 */
abstract class CustomException: Exception()
class BadRequestException(override val message: String): CustomException()
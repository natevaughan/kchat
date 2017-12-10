package com.natevaughan.hat.framework

/**
 * Created by nate on 11/22/17
 */
abstract class CustomException: Exception()
class UnauthorizedException(override val message: String): CustomException()
class BadRequestException(override val message: String): CustomException()
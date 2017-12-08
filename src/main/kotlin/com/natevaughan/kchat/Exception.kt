package com.natevaughan.kchat

import javax.ws.rs.NotFoundException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

/**
 * Created by nate on 11/22/17
 */
abstract class CustomException: Exception()
class UnauthorizedException(override val message: String): CustomException()
class BadRequestException(override val message: String): CustomException()
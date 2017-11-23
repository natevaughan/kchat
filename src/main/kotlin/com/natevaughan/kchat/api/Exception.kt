package com.natevaughan.kchat.api

import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

/**
 * Created by nate on 11/22/17
 */
abstract class CustomException: Exception()
class UnauthorizedException(override val message: String): CustomException()
class BadRequestException(override val message: String): CustomException()

@Provider
class ExceptionHandler : ExceptionMapper<CustomException> {
    override fun toResponse(exception: CustomException): Response {
        return when (exception) {
            is UnauthorizedException -> UNAUTHORIZED
            is BadRequestException -> BAD_REQUEST
            else -> {
                INTERNAL_SERVER_ERROR
            }
        }
    }
}
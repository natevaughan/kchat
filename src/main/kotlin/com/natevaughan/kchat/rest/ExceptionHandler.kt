package com.natevaughan.kchat.rest

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.natevaughan.kchat.framework.BadRequestException
import com.natevaughan.kchat.framework.NotFoundException
import com.natevaughan.kchat.framework.UnauthorizedException
import com.natevaughan.kchat.util.ExceptionMessageTranslator.missingParam
import com.natevaughan.kchat.util.logger
import java.lang.Exception
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper

/**
 * Created by nate on 3/21/19
 */
class ServerErrorMapper : ExceptionMapper<Exception> {

	val log = logger(this)

	override fun toResponse(ex: Exception): Response {
		return when (ex) {
			is MissingKotlinParameterException -> Response.status(Response.Status.BAD_REQUEST)
					.entity(RestException(missingParam(ex.message)))
					.type(MediaType.APPLICATION_JSON).build()
			is NotFoundException -> Response.status(Response.Status.NOT_FOUND)
					.entity(RestException(ex.message))
					.type(MediaType.APPLICATION_JSON).build()
			is javax.ws.rs.NotFoundException -> Response.status(Response.Status.NOT_FOUND)
					.entity(RestException(ex.message))
					.type(MediaType.APPLICATION_JSON).build()
			is UnauthorizedException -> Response.status(Response.Status.UNAUTHORIZED)
					.entity(RestException(ex.message))
					.type(MediaType.APPLICATION_JSON).build()
			is BadRequestException -> Response.status(Response.Status.BAD_REQUEST)
					.entity(RestException(ex.message))
					.type(MediaType.APPLICATION_JSON).build()
			else -> {
				log.error("API error: ${ex.message}", ex)
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity(RestServerError(ex.message, ex.javaClass.simpleName, ex.cause?.javaClass?.simpleName))
						.type(MediaType.APPLICATION_JSON).build()
			}
		}
	}
}

class RestServerError(val errorMessage: String?, val exception: String?, val cause: String?)
class RestException(val errorMessage: String?)
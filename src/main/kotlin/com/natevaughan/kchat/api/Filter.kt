package com.natevaughan.kchat.api

/**
 * Created by nate on 11/22/17
 */


import com.natevaughan.kchat.api.SecurityFilterObj.KEY_API_TOKEN
import com.natevaughan.kchat.api.SecurityFilterObj.log
import com.natevaughan.kchat.api.SecurityFilterObj.users
import com.natevaughan.kchat.model.Role
import com.natevaughan.kchat.model.User
import com.natevaughan.kchat.security.MySecurityContext
import org.slf4j.LoggerFactory
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.container.ContainerResponseContext
import javax.ws.rs.container.ContainerResponseFilter
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

object SecurityFilterObj {
    val log = LoggerFactory.getLogger(this::class.java)
    val KEY_API_TOKEN = "api-token"
    val users = arrayListOf(User(name = "nate", role = Role.ADMIN, apiKey = "7ddf32e17a6ac5ce04a8ecbf782ca509"))
}

class PoweredByResponseFilter : ContainerResponseFilter {
    override fun filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext ) {
        responseContext.getHeaders().add("X-Powered-By", "Jersey :-)");
    }
}

class SecurityFilter : ContainerRequestFilter {

    override fun filter(request: ContainerRequestContext) {
        val tokens = request.headers[KEY_API_TOKEN]
        if (tokens != null) {
            val user = users.find { tokens.contains( it.apiKey ) }

            if (user != null) {
                request.securityContext = MySecurityContext(user)
                return
            }
        }
        log.info("Unauthorized access attempted: ${request.headers}")
        throw UnauthorizedException("Api key not found or missing")
    }
}

@Provider
class ExceptionHandler : ExceptionMapper<CustomException> {
    override fun toResponse(exception: CustomException): Response {
        return when (exception) {
            is UnauthorizedException -> Response.status(Response.Status.UNAUTHORIZED).build()
            is BadRequestException -> Response.status(Response.Status.BAD_REQUEST).build()
            else -> {
                Response.status(Response.Status.INTERNAL_SERVER_ERROR).build()
            }
        }
    }
}
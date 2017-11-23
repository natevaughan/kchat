package com.natevaughan.kchat.api

import com.natevaughan.kchat.model.message.user.Role
import com.natevaughan.kchat.model.message.user.User
import com.natevaughan.kchat.MySecurityContext
import com.natevaughan.kchat.model.message.user.HibernateUserRepo
import com.natevaughan.kchat.model.message.user.UserRepo
import org.slf4j.LoggerFactory
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.container.ContainerResponseContext
import javax.ws.rs.container.ContainerResponseFilter
import javax.ws.rs.core.Response


val KEY_API_TOKEN = "api-token"

// 2xx
val CREATED = Response.status(Response.Status.CREATED).build()

// 4xx
val BAD_REQUEST = Response.status(Response.Status.BAD_REQUEST).build()
val UNAUTHORIZED = Response.status(Response.Status.UNAUTHORIZED).build()

// 5xx
val INTERNAL_SERVER_ERROR = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build()


/**
 * Created by nate on 11/22/17
 */
val log = LoggerFactory.getLogger(SecurityFilter::class.java)
val users = arrayListOf(User(name = "nate", role = Role.ADMIN, apiKey = "7ddf32e17a6ac5ce04a8ecbf782ca509"))
val userRepo: UserRepo = HibernateUserRepo()

class PoweredByResponseFilter : ContainerResponseFilter {
    override fun filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext ) {
        responseContext.getHeaders().add("X-Powered-By", "Jersey :-)");
    }
}

class SecurityFilter : ContainerRequestFilter {
    override fun filter(request: ContainerRequestContext) {
        val tokens = request.headers[KEY_API_TOKEN]
        if (tokens != null) {

//            val user = users.find { tokens.contains( it.apiKey ) }
            val user = userRepo.findByApiKey(tokens.first())

            request.securityContext = MySecurityContext(user)
            return
        }
        log.info("Unauthorized access attempted: ${request.headers}")
        throw UnauthorizedException("UserCtrl key not found or missing")
    }
}
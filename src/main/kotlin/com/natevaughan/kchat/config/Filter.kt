package com.natevaughan.kchat.config

import com.natevaughan.kchat.framework.UnauthorizedException
import com.natevaughan.kchat.user.UserService
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.ws.rs.NotFoundException
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.container.ContainerResponseContext
import javax.ws.rs.container.ContainerResponseFilter

/**
 * Created by nate on 11/22/17
 */
val KEY_API_TOKEN = "api-token"
val log = LoggerFactory.getLogger(SecurityFilter::class.java)

class PoweredByResponseFilter : ContainerResponseFilter {
    override fun filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext ) {
        responseContext.getHeaders().add("X-Powered-By", "Jersey :-)")
    }
}

class SecurityFilter @Inject constructor(val userService: UserService) : ContainerRequestFilter {
    override fun filter(request: ContainerRequestContext) {
        val tokens = request.headers[KEY_API_TOKEN]
        if (tokens != null) {

            try {
                val user = userService.findByApiKey(tokens.first())
                request.securityContext = MySecurityContext(user)
                return
            } catch (e: NotFoundException) {
            }
        }
        if (request.uriInfo.path == "health-check") {
            return
        }
        log.warn("Unauthorized access attempted: ${request.headers}")
        throw UnauthorizedException("UserCtrl accessKey not found or missing")
    }
}
package com.natevaughan.kchat.rest

import com.natevaughan.kchat.framework.UnauthorizedException
import com.natevaughan.kchat.service.UserService
import com.natevaughan.kchat.util.logger
import javax.inject.Inject
import javax.ws.rs.NotFoundException
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.container.ContainerResponseContext
import javax.ws.rs.container.ContainerResponseFilter
import javax.ws.rs.core.MultivaluedMap

/**
 * Created by nate on 11/22/17
 */
class PoweredByResponseFilter : ContainerResponseFilter {
    override fun filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext ) {
        responseContext.getHeaders().add("X-Software-Version", "0.1.0")
    }
}

class SecurityFilter @Inject constructor(val userService: UserService) : ContainerRequestFilter {

	val KEY_API_TOKEN = "api-token"
	val KEY_SESSION_ID = "JSESSIONID"
	val log = logger(this)
	val whitelistedPaths = setOf("health-check", "invite/redeem", "")

	override fun filter(request: ContainerRequestContext) {
        val tokens = request.headers[KEY_API_TOKEN]
		log.info("${getClientIpAddr(request.headers)} ${request.cookies.get(KEY_SESSION_ID)?.value} ${request.method} ${request.uriInfo.path}")
        if (tokens != null) {
            try {
                val user = userService.findByApiKey(tokens.first())
                request.securityContext = MySecurityContext(user)
                return
            } catch (e: NotFoundException) {
            }
        }
        if (request.uriInfo.path in whitelistedPaths) {
            return
        }
        log.warn("Unauthorized access attempted: ${request.headers}")
        throw UnauthorizedException("AccessKey not found or missing")
    }

	val keys = arrayOf("X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR")
	val keysSize = keys.size - 1

	private fun getClientIpAddr(headers: MultivaluedMap<String, String>): String? {
		var i = 0
		var ip: String?
		do {
			ip = headers.get(keys[i])?.first()
			if (ip == null) {
				ip = headers.get(keys[i].toLowerCase())?.first()
			}
			i++
		} while ((ip == null || ip.isEmpty() || "unknown".equals(ip, ignoreCase = true)) && i <= keysSize)
		return ip
	}
}
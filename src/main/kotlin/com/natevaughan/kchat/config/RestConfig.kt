package com.natevaughan.kchat.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.framework.BAD_REQUEST
import com.natevaughan.kchat.framework.CustomException
import com.natevaughan.kchat.framework.INTERNAL_SERVER_ERROR
import com.natevaughan.kchat.framework.NOT_FOUND
import com.natevaughan.kchat.framework.UNAUTHORIZED
import com.natevaughan.kchat.framework.UnauthorizedException
import com.natevaughan.kchat.framework.UtilityCtrl
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.server.ResourceConfig
import java.security.Principal
import javax.ws.rs.BadRequestException
import javax.ws.rs.NotFoundException
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

/**
 * Created by nate on 11/25/17
 */
class RestConfig : ResourceConfig() {
    init {

        val mapper = ObjectMapper()
                .registerModule(KotlinModule())
                .disable(SerializationFeature.INDENT_OUTPUT)

        val jsonProvider = JacksonJaxbJsonProvider()
        jsonProvider.setMapper(mapper)

        register(jsonProvider)
        register(JacksonFeature::class.java)
        register(PoweredByResponseFilter::class.java)
        register(ExceptionHandler::class.java)
        register(UtilityCtrl::class.java)
    }

}

class  MySecurityContext(val user: User): SecurityContext {

    override fun isUserInRole(role: String?): Boolean {
        return true // check roles if you need ...
    }

    override fun getAuthenticationScheme(): String? {
        return null // ...
    }

    override fun getUserPrincipal(): Principal {
        return user // return your user here - User must implement Principal
    }

    override fun isSecure(): Boolean {
        return false // check HTTPS
    }
}

@Provider
class ExceptionHandler : ExceptionMapper<CustomException> {
    override fun toResponse(exception: CustomException): Response {
        return when (exception) {
            is UnauthorizedException -> UNAUTHORIZED
            is BadRequestException -> BAD_REQUEST
            is NotFoundException -> NOT_FOUND
            else -> {
                INTERNAL_SERVER_ERROR
            }
        }
    }
}
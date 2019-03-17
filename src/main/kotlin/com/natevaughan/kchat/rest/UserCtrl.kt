package com.natevaughan.kchat.rest

import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.framework.UnauthorizedException
import com.natevaughan.kchat.service.UserService
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

/**
 * Created by nate on 11/22/17
 */
@Singleton
@Path("user")
class UserCtrl @Inject constructor(val userService: UserService) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun echo(@Context sc: SecurityContext): User {
        return sc.userPrincipal as User
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun createUser(user: User, @Context sc: SecurityContext): ApiKey {
        val requester = sc.userPrincipal as User
        if (requester.role != User.Role.ADMIN) {
            throw UnauthorizedException("Must have creator role")
        }
        val newUser = user.copy(UUID.randomUUID().toString())
        return ApiKey(userService.save(newUser).apiKey)
    }
}

class ApiKey(val apiKey: String)

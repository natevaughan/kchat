package com.natevaughan.kchat.model.user

import com.natevaughan.kchat.UnauthorizedException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

/**
 * Created by nate on 11/22/17
 */
@Singleton
@Path("/user")
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
        if (requester.role != Role.ADMIN) {
            throw UnauthorizedException("Must have admin role")
        }
        val newUser = user.copy(apiKey = UUID.randomUUID().toString())
        return ApiKey(userService.save(newUser).apiKey)
    }

}

class ApiKey(val apiKey: String)

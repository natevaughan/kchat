package com.natevaughan.kchat.model.message.user

import com.natevaughan.kchat.api.ApiKey
import com.natevaughan.kchat.api.UnauthorizedException
import java.util.*
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
class UserCtrl {

    val userRepo: UserRepo = CacheUserRepo.getInstance()

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun createUser(user: User, @Context sc: SecurityContext): ApiKey {
        val requester = sc.userPrincipal as User
        if (requester.role != Role.ADMIN) {
            throw UnauthorizedException("Must have admin role")
        }
        val newUser = user.copy(apiKey = UUID.randomUUID().toString())
        return ApiKey(userRepo.save(newUser).apiKey)
    }
}
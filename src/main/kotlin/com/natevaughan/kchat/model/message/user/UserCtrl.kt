package com.natevaughan.kchat.model.message.user

import com.natevaughan.kchat.api.CREATED
import com.natevaughan.kchat.api.CreateChat
import com.natevaughan.kchat.api.UnauthorizedException
import javax.inject.Singleton
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext

/**
 * Created by nate on 11/22/17
 */
@Singleton
@Path("/user")
class UserCtrl {

    val userRepo: UserRepo = HibernateUserRepo()

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun createUser(user: User, @Context sc: SecurityContext): Response {
        val requester = sc.userPrincipal as User
        if (requester.role != Role.ADMIN) {
            throw UnauthorizedException("Must have admin role")
        }
        userRepo.save(user)
        return CREATED
    }
}
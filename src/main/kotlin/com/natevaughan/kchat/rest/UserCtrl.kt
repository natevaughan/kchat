package com.natevaughan.kchat.rest

import com.natevaughan.kchat.api.Participant
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.framework.UnauthorizedException
import com.natevaughan.kchat.service.UserService
import com.natevaughan.kchat.util.TokenGenerator.generateApiKey
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
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
    fun createUser(user: CreateUserRequest, @Context sc: SecurityContext): ApiKeyResponse {
        val requester = sc.userPrincipal as User
        if (requester.role != User.Role.ADMIN) {
            throw UnauthorizedException("Must have admin role")
        }
        val apiKey = generateApiKey()
        val newUser = User(UUID.randomUUID().toString(), Instant.now(), user.name, User.Role.USER, apiKey)
        userService.save(newUser)
        return ApiKeyResponse(newUser.name!!, apiKey)
    }

	@GET
	@Path("space/{spaceId}")
	@Produces(MediaType.APPLICATION_JSON)
	fun listUsersForSpace(@PathParam("spaceId") spaceId: String, @Context sc: SecurityContext): Collection<Participant> {
		val user = sc.userPrincipal as User
		return userService.listForSpace(spaceId, user)
	}
}

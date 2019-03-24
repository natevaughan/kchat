package com.natevaughan.kchat.rest

import com.natevaughan.kchat.api.Invite
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.service.InviteService
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
 * Created by nate on 3/21/19
 */
@Singleton
@Path("invite")
class InviteCtrl @Inject constructor(private val inviteService: InviteService) {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	fun listForUser(@Context sc: SecurityContext): List<Invite> {
		val user =  sc.userPrincipal as User
		return inviteService.listForUser(user)
	}

	@POST
	@Path("redeem")
	@Produces(MediaType.APPLICATION_JSON)
	fun redeem(token: TokenRedemptionRequest, @Context sc: SecurityContext): ApiKeyResponse {
		val user =  sc.userPrincipal as User?
		val updatedUser = inviteService.redeem(token.token, user, token.alias)
		return ApiKeyResponse(updatedUser.name!!, updatedUser.apiKey!!)
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	fun create(invite: CreateInviteRequest, @Context sc: SecurityContext): Invite {
		val user =  sc.userPrincipal as User
		return inviteService.create(invite.spaceId, invite.memo, user)
	}
}
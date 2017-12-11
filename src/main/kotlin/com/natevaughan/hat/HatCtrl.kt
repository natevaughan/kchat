package com.natevaughan.hat


import com.natevaughan.hat.framework.CREATED
import com.natevaughan.hat.framework.OK
import com.natevaughan.hat.message.Message
import com.natevaughan.hat.message.MessageService
import com.natevaughan.hat.message.RestMessage
import com.natevaughan.hat.user.User
import javax.inject.Inject
import javax.inject.Singleton
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext

/**
 * Created by nate on 11/22/17
 */
@Singleton
@Path("hat")
class HatCtrl @Inject constructor(val hatService: HatService, val messageService: MessageService) {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun listForUser(@Context sc: SecurityContext): Collection<Hat> {
        return hatService.listForUser(sc.userPrincipal as User)
    }

    @GET
    @Path("{hatId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun message(@PathParam("hatId") hatId: Long, @Context sc: SecurityContext): Hat {
        return hatService.findById(hatId, sc.userPrincipal as User)
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun create(body: RestHat, @Context sc: SecurityContext): Hat {
        return hatService.create(body.name, sc.userPrincipal as User, body.participants)
    }

    @DELETE
    @Path("{hatId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun delete(@PathParam("hatId") id: Long, @Context sc: SecurityContext): Response {
        val user = sc.userPrincipal as User
        hatService.delete(id, user)
        return OK
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{hatId}/message")
    fun create(@PathParam("hatId") hatId: Long, message: RestMessage, @Context sc: SecurityContext): Response {
        val user =  sc.userPrincipal as User
        messageService.create(message.text, hatId, user)
        return CREATED
    }

    @GET
    @Path("{hatId}/since/{timestamp}")
    @Produces(MediaType.APPLICATION_JSON)
    fun since(@PathParam("hatId") hatId: Long, @PathParam("timestamp") timestamp: Long, @Context sc: SecurityContext): Iterable<Message> {
        return messageService.findAllSinceTimestamp(hatId, timestamp, sc.userPrincipal as User)
    }

    @GET
    @Path("{hatId}/previous/{count}")
    @Produces(MediaType.APPLICATION_JSON)
    fun previous(@PathParam("hatId") hatId: Long, @PathParam("count") count: Int, @Context sc: SecurityContext): Iterable<Message> {
        return messageService.findRecent(hatId, count, sc.userPrincipal as User)
    }

}

data class RestHat(val name: String, val participants: Collection<Long>)
package com.natevaughan.hat.message

import com.natevaughan.hat.framework.CREATED
import com.natevaughan.hat.framework.OK
import com.natevaughan.hat.user.User
import javax.inject.Inject
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
@Path("message")
class MessageCtrl @Inject constructor(val messageService: MessageService) {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun message(@PathParam("id") id: Long, @Context sc: SecurityContext): Message {
        val msg = messageService.findById(id)
        if (msg != null) {
            return msg
        }
        throw NotFoundException("Message not found: $id")
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun updateMessage(@PathParam("id") id: Long, message: RestMessage, @Context sc: SecurityContext): Message {
        val user =  sc.userPrincipal as User
        return messageService.update(message.text, id, user)
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun deleteMessage(@PathParam("id") id: Long, @Context sc: SecurityContext): Response {
        messageService.delete(id, sc.userPrincipal as User)
        return OK
    }
}

data class RestMessage(val text: String)

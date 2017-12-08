package com.natevaughan.kchat.model.message

import com.natevaughan.kchat.CREATED
import com.natevaughan.kchat.OK
import com.natevaughan.kchat.model.user.User
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
@Path("/message")
class MessageCtrl @Inject constructor(val messageService: MessageService) {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun message(@PathParam("id") id: Long, @Context sc: SecurityContext): Message {
        val msg = messageService.findById(id)
        if (msg != null) {
            return msg
        }
        throw NotFoundException("Message not found: $id")
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun create(message: RestMessage, @Context sc: SecurityContext): Response {
        val msg = Message(text = message.text, author = sc.userPrincipal as User, timestamp = System.currentTimeMillis())
        messageService.create(msg)
        return CREATED
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun updateMessage(@PathParam("id") id: Long, message: RestMessage, @Context sc: SecurityContext): Message {
        val msg = Message(message.text, id, sc.userPrincipal as User)
        return messageService.update(msg, id, sc.userPrincipal as User)
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun deleteMessage(@PathParam("id") id: Long, @Context sc: SecurityContext): Response {
        messageService.delete(id, sc.userPrincipal as User)
        return OK
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun myMessages(@Context sc: SecurityContext): Iterable<Message> {
        return messageService.findRecent(20)
    }

    @GET
    @Path("/since/{timestamp}")
    @Produces(MediaType.APPLICATION_JSON)
    fun since(@PathParam("timestamp") timestamp: Long, @Context sc: SecurityContext): Iterable<Message> {
        return messageService.findAllSinceTimestamp(timestamp)
    }

    @GET
    @Path("/previous/{count}")
    @Produces(MediaType.APPLICATION_JSON)
    fun previous(@PathParam("count") count: Int, @Context sc: SecurityContext): Iterable<Message> {
        return messageService.findRecent(count)
    }

}

class RestMessage(val text: String)

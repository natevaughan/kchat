package com.natevaughan.kchat.model.message

import com.natevaughan.kchat.api.CREATED
import com.natevaughan.kchat.api.RestMessage
import com.natevaughan.kchat.api.UnauthorizedException
import com.natevaughan.kchat.model.message.user.User
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
class MessageCtrl {

    val messageRepo: MessageRepo = CacheMessageRepo()

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun message(@PathParam("id") id: Long, @Context sc: SecurityContext): Message {
        return messageRepo.findById(id)
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun updateMessage(@PathParam("id") id: Long, message: RestMessage, @Context sc: SecurityContext): Message {
        val old = messageRepo.findById(id)
        if (old.author != sc.userPrincipal as User) {
            throw UnauthorizedException("Requester/author mismatch")
        }
        val new = old.copy(text = message.text)
        return messageRepo.save(new)
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun myMessages(@Context sc: SecurityContext): Iterable<Message> {
        return messageRepo.findAllByUser(sc.userPrincipal as User)
    }
    @GET
    @Path("/since/{timestamp}")
    @Produces(MediaType.APPLICATION_JSON)
    fun since(@PathParam("timestamp") timestamp: Long, @Context sc: SecurityContext): Iterable<Message> {
        return messageRepo.findAllSinceTimestamp(timestamp)
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun create(message: RestMessage, @Context sc: SecurityContext): Response {
        val msg = Message(text = message.text, author = sc.userPrincipal as User, timestamp = System.currentTimeMillis())
        messageRepo.save(msg)
        return CREATED
    }
}
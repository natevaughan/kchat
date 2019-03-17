package com.natevaughan.kchat.rest


import com.natevaughan.kchat.api.Chat
import com.natevaughan.kchat.api.Message
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.framework.CREATED
import com.natevaughan.kchat.framework.OK
import com.natevaughan.kchat.service.ChatService
import com.natevaughan.kchat.service.MessageService
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
class ChatCtrl @Inject constructor(val chatService: ChatService, val messageService: MessageService) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun listForUser(@Context sc: SecurityContext): Collection<Chat> {
        return chatService.listForUser(sc.userPrincipal as User)
    }

    @GET
    @Path("{chatId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun message(@PathParam("chatId") chatId: Long, @Context sc: SecurityContext): Chat {
        return chatService.findById(chatId, sc.userPrincipal as User)
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun create(body: RestChat, @Context sc: SecurityContext): Chat {
        return chatService.create(body.name, sc.userPrincipal as User, body.participants)
    }

    @DELETE
    @Path("{chatId}")
    @Produces(MediaType.APPLICATION_JSON)
    fun delete(@PathParam("chatId") chatId: Long, @Context sc: SecurityContext): Response {
        val user = sc.userPrincipal as User
        chatService.delete(chatId, user)
        return OK
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{chatId}/message")
    fun create(@PathParam("chatId") chatId: Long, message: RestMessage, @Context sc: SecurityContext): Response {
        val user =  sc.userPrincipal as User
        messageService.create(message.text, chatId, user)
        return CREATED
    }

    @GET
    @Path("{chatId}/since/{timestamp}")
    @Produces(MediaType.APPLICATION_JSON)
    fun since(@PathParam("chatId") chatId: Long, @PathParam("timestamp") timestamp: Long, @Context sc: SecurityContext): Iterable<Message> {
        return messageService.findAllSinceTimestamp(chatId, timestamp, sc.userPrincipal as User)
    }

    @GET
    @Path("{hatId}/previous/{count}")
    @Produces(MediaType.APPLICATION_JSON)
    fun previous(@PathParam("hatId") chatId: Long, @PathParam("count") count: Int, @Context sc: SecurityContext): Iterable<Message> {
        return messageService.findRecent(chatId, count, sc.userPrincipal as User)
    }

}

data class RestChat(val name: String, val participants: Collection<Long>)
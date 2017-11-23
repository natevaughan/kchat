package com.natevaughan.kchat.api

import com.natevaughan.kchat.api.SecurityFilterObj.users
import com.natevaughan.kchat.model.Message
import com.natevaughan.kchat.model.User
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.SecurityContext

/**
 * Created by nate on 11/22/17
 */
@Path("/")
class Api {

    @GET
    @Path("message/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getMessages(@PathParam("id") id: String, @Context sc: SecurityContext): Array<Message> {
        return arrayOf(Message(author = sc.userPrincipal as User, text = "hello, world $id"))
    }

    @POST
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    fun createUser(user: User, @Context sc: SecurityContext) {
        users.add(user)
    }
}
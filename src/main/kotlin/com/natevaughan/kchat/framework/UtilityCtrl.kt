package com.natevaughan.kchat.framework

import java.util.concurrent.atomic.AtomicLong
import javax.inject.Singleton
import javax.ws.rs.GET
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
@Path("/")
class UtilityCtrl {

    val count = AtomicLong()
    val map = mutableMapOf<String, Any>()

    @GET
    @Produces(MediaType.TEXT_HTML)
    fun base(@Context sc: SecurityContext): String {
        return this::class.java.getResource("/index.html").readText()


    }

    @GET
    @Path("health-check")
    @Produces(MediaType.APPLICATION_JSON)
    fun healthCheck(@PathParam("id") id: Long, @Context sc: SecurityContext): Map<String, Any> {
        map["healthyCount"] = count.incrementAndGet()
        return map
    }
}

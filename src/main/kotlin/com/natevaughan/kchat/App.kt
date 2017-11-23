@file:JvmName("App")
package com.natevaughan.kchat

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.natevaughan.kchat.AppCompanion.log
import com.natevaughan.kchat.api.Api
import com.natevaughan.kchat.api.ExceptionHandler
import com.natevaughan.kchat.api.PoweredByResponseFilter
import com.natevaughan.kchat.api.SecurityFilter
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig
import org.slf4j.LoggerFactory
import java.io.InputStreamReader
import javax.ws.rs.core.UriBuilder
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider



/**
 * Created by nate on 11/22/17
 */
object AppCompanion {
    val log = LoggerFactory.getLogger(this::class.java)

}

fun main(args: Array<String>) {

    log.info("starting app configuration")

    val mapper = ObjectMapper()
        .registerModule(KotlinModule())
        .enable(SerializationFeature.INDENT_OUTPUT)

    val provider = JacksonJaxbJsonProvider()
    provider.setMapper(mapper)

    log.info("congiuring ReST...")
    val uri = UriBuilder.fromUri("http://localhost/").port(8080).build()
    val config = ResourceConfig(
            Api::class.java,
            ExceptionHandler::class.java,
            PoweredByResponseFilter::class.java,
            SecurityFilter::class.java,
            JacksonFeature::class.java
    )

    config.register(provider)

    log.info("starting server...")
    val server = JdkHttpServerFactory.createHttpServer(uri, config)

    log.info("App started at ${uri.host}:${uri.port}. Press <enter> to terminate.")

    val reader = InputStreamReader(System.`in`)
    reader.read()
    server.stop(0)
}
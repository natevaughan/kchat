@file:JvmName("App")
package com.natevaughan.kchat

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.natevaughan.kchat.AppCompanion.log
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig
import org.slf4j.LoggerFactory
import java.io.InputStreamReader
import javax.ws.rs.core.UriBuilder
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider
import com.natevaughan.kchat.api.*
import com.natevaughan.kchat.model.chat.UtilityCtrl
import com.natevaughan.kchat.model.message.MessageCtrl
import com.natevaughan.kchat.model.message.user.UserCtrl
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.overriding


/**
 * Created by nate on 11/22/17
 */
object AppCompanion {
    val log = LoggerFactory.getLogger(this::class.java)

}

fun main(args: Array<String>) {

    log.info("loading configurations")

    val appConfig = ConfigurationProperties.systemProperties() overriding
            EnvironmentVariables() overriding
            ConfigurationProperties.fromResource("defaults.properties")

    log.info("starting app configuration")

    val mapper = ObjectMapper()
        .registerModule(KotlinModule())
        .enable(SerializationFeature.INDENT_OUTPUT)

    val jsonProvider = JacksonJaxbJsonProvider()
    jsonProvider.setMapper(mapper)

    log.info("congiuring ReST...")

    val resources = ResourceConfig(
            UtilityCtrl::class.java,
            UserCtrl::class.java,
            MessageCtrl::class.java,
            ExceptionHandler::class.java,
            PoweredByResponseFilter::class.java,
            SecurityFilter::class.java,
            JacksonFeature::class.java
    )

    val uri = UriBuilder
            .fromUri(appConfig.get(server.host))
            .port(appConfig.get(server.port)).build()
    resources.register(jsonProvider)

    log.info("starting server...")
    val server = JdkHttpServerFactory.createHttpServer(uri, resources)

    log.info("App started at ${uri.host}:${uri.port}. Press <enter> to terminate.")

    val reader = InputStreamReader(System.`in`)
    reader.read()
    server.stop(0)
}
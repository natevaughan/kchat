@file:JvmName("App")
package com.natevaughan.hat

import com.google.inject.Guice
import com.natevaughan.hat.AppCompanion.log
import org.slf4j.LoggerFactory
import java.io.InputStreamReader
import javax.ws.rs.core.UriBuilder
import com.natevaughan.hat.AppCompanion.KEY_DEFAULT_PROPERTIES_FILE
import com.natevaughan.hat.config.ServiceModule
import com.natevaughan.hat.config.RestConfig
import com.natevaughan.hat.config.SecurityFilter
import com.natevaughan.hat.config.server
import com.natevaughan.hat.message.MessageCtrl
import com.natevaughan.hat.user.UserCtrl
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.overriding
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory

/**
 * Created by nate on 11/22/17
 */
object AppCompanion {
    val log = LoggerFactory.getLogger(this::class.java)
    val KEY_DEFAULT_PROPERTIES_FILE = "defaults.properties"

}

fun main(args: Array<String>) {

    log.info("loading configurations")

    val appConfig = ConfigurationProperties.systemProperties() overriding
            EnvironmentVariables() overriding
            ConfigurationProperties.fromResource(KEY_DEFAULT_PROPERTIES_FILE)

    log.info("starting app configuration")

    val injector = Guice.createInjector(ServiceModule(appConfig))

    val rc = RestConfig()
    rc.register(injector.getInstance(UserCtrl::class.java))
    rc.register(injector.getInstance(MessageCtrl::class.java))
    rc.register(injector.getInstance(SecurityFilter::class.java))


    val uri = UriBuilder
            .fromUri(appConfig.get(server.host))
            .port(appConfig.get(server.port)).build()

    log.info("starting server...")
    val server = GrizzlyHttpServerFactory.createHttpServer(uri, rc)

    log.info("App started at ${uri.host}:${uri.port}. Press <enter> to terminate.")

    val reader = InputStreamReader(System.`in`)
    reader.read()
    server.shutdownNow()
}
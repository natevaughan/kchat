@file:JvmName("App")
package com.natevaughan.kchat

//import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory
import com.google.inject.Guice
import com.natevaughan.kchat.AppCompanion.KEY_DEFAULT_PROPERTIES_FILE
import com.natevaughan.kchat.AppCompanion.log
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.api.UserRepo
import com.natevaughan.kchat.config.RestConfig
import com.natevaughan.kchat.config.SecurityFilter
import com.natevaughan.kchat.config.ServiceModule
import com.natevaughan.kchat.config.admin
import com.natevaughan.kchat.config.server
import com.natevaughan.kchat.message.MessageCtrl
import com.natevaughan.kchat.user.UserCtrl
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.overriding
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.slf4j.LoggerFactory
import java.io.InputStreamReader
import javax.ws.rs.core.UriBuilder

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

    // set up admin user
    val userRepo = injector.getInstance(UserRepo::class.java)
    val existingAdmin = userRepo.findByApiKey(appConfig.get(admin.token))

    if (existingAdmin == null) {
        val admin = User(
                appConfig.get(admin.name),
                User.Role.ADMIN,
                appConfig.get(admin.token)
        )
        userRepo.save(admin)
    }

    val rc = RestConfig()
    rc.register(injector.getInstance(ChatCtrl::class.java))
    rc.register(injector.getInstance(UserCtrl::class.java))
    rc.register(injector.getInstance(MessageCtrl::class.java))
    rc.register(injector.getInstance(SecurityFilter::class.java))

    val uri = UriBuilder
            .fromUri(appConfig.get(server.host))
            .port(appConfig.get(server.port)).build()

    log.info("starting server...")
    val server = GrizzlyHttpServerFactory.createHttpServer(uri, rc)
    // val server = JdkHttpServerFactory.createHttpServer(uri, rc)

    log.info("App started at ${uri.host}:${uri.port}. Press <enter> to terminate.")

    val reader = InputStreamReader(System.`in`)
    reader.read()
    server.shutdownNow()
    //server.stop(0)
}
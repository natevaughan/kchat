@file:JvmName("App")
package com.natevaughan.kchat

//import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory
import com.google.inject.Guice
import com.natevaughan.kchat.AppCompanion.KEY_DEFAULT_PROPERTIES_FILE
import com.natevaughan.kchat.AppCompanion.log
import com.natevaughan.kchat.api.ConnectionPool
import com.natevaughan.kchat.api.User
import com.natevaughan.kchat.config.ServiceModule
import com.natevaughan.kchat.config.admin
import com.natevaughan.kchat.config.server
import com.natevaughan.kchat.framework.NotFoundException
import com.natevaughan.kchat.rest.InviteCtrl
import com.natevaughan.kchat.rest.MessageCtrl
import com.natevaughan.kchat.rest.RestConfig
import com.natevaughan.kchat.rest.SecurityFilter
import com.natevaughan.kchat.rest.SpaceCtrl
import com.natevaughan.kchat.rest.UserCtrl
import com.natevaughan.kchat.service.UserService
import com.natevaughan.kchat.util.logger
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.overriding
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import java.io.InputStreamReader
import java.time.Instant
import java.util.UUID
import javax.ws.rs.core.UriBuilder

/**
 * Created by nate on 11/22/17
 */
object AppCompanion {
	val log = logger(this)
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
    val userService = injector.getInstance(UserService::class.java)
	try {
		val existingAdmin = userService.findByApiKey(appConfig.get(admin.token))
	} catch (e: NotFoundException) {
        val admin = User(
				UUID.randomUUID().toString(),
				Instant.now(),
                appConfig.get(admin.name),
                User.Role.ADMIN,
                appConfig.get(admin.token)
        )
        userService.save(admin)
    }

    val rc = RestConfig()
    rc.register(injector.getInstance(UserCtrl::class.java))
    rc.register(injector.getInstance(MessageCtrl::class.java))
    rc.register(injector.getInstance(InviteCtrl::class.java))
    rc.register(injector.getInstance(SpaceCtrl::class.java))
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

	// shutdown
	injector.getInstance(ConnectionPool::class.java).close()
    server.shutdownNow()
    //server.stop(0)
}
package com.natevaughan.kchat.config

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.natevaughan.kchat.ChatCtrl
import com.natevaughan.kchat.ChatService
import com.natevaughan.kchat.api.ChatRepo
import com.natevaughan.kchat.api.MessageRepo
import com.natevaughan.kchat.api.UserRepo
import com.natevaughan.kchat.domain.HibernateChatRepo
import com.natevaughan.kchat.domain.HibernateMessageRepo
import com.natevaughan.kchat.domain.HibernateUserRepo
import com.natevaughan.kchat.message.MessageCtrl
import com.natevaughan.kchat.message.MessageService
import com.natevaughan.kchat.user.UserCtrl
import com.natevaughan.kchat.user.UserService
import com.natpryce.konfig.Configuration
import org.hibernate.cfg.AvailableSettings.JPA_JDBC_DRIVER
import org.hibernate.cfg.AvailableSettings.JPA_JDBC_PASSWORD
import org.hibernate.cfg.AvailableSettings.JPA_JDBC_URL
import org.hibernate.cfg.AvailableSettings.JPA_JDBC_USER
import org.hibernate.cfg.AvailableSettings.POOL_SIZE
import javax.persistence.EntityManagerFactory

/**
 * Created by nate on 11/26/17
 */
class ServiceModule(val appConfig: Configuration) : AbstractModule() {

    val dataSource = DataSourceBuilder()

    init {
        dataSource.put(JPA_JDBC_DRIVER, appConfig.get(jdbc.driver))
                .put(JPA_JDBC_URL, appConfig.get(jdbc.url))
                .put(JPA_JDBC_USER, appConfig.get(jdbc.user))
                .put(JPA_JDBC_PASSWORD, appConfig.get(jdbc.pass))
                .put(POOL_SIZE, appConfig.get(jdbc.pool))
    }

    override fun configure() {
        bind(UserCtrl::class.java)
        bind(UserService::class.java)
        bind(UserRepo::class.java).to(HibernateUserRepo::class.java)

        bind(MessageCtrl::class.java)
        bind(MessageService::class.java)
        bind(MessageRepo::class.java).to(HibernateMessageRepo::class.java)

        bind(ChatCtrl::class.java)
        bind(ChatService::class.java)
        bind(ChatRepo::class.java).to(HibernateChatRepo::class.java)

        bind(SecurityFilter::class.java)
    }

    @Provides
    fun provideEntityManager(): EntityManagerFactory {
        return dataSource.entityManagerFactory()
    }

    @Provides
    fun provideConfiguration(): Configuration {
        return appConfig
    }

}
package com.natevaughan.kchat.config

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.natevaughan.kchat.model.message.HibernateMessageRepo
import com.natevaughan.kchat.model.message.MessageCtrl
import com.natevaughan.kchat.model.message.MessageRepo
import com.natevaughan.kchat.model.message.MessageService
import com.natevaughan.kchat.model.user.HibernateUserRepo
import com.natevaughan.kchat.model.user.UserCtrl
import com.natevaughan.kchat.model.user.UserRepo
import com.natevaughan.kchat.model.user.UserService
import com.natpryce.konfig.Configuration
import javax.persistence.EntityManagerFactory

/**
 * Created by nate on 11/26/17
 */
class ServiceModule(val appConfig: Configuration) : AbstractModule() {

    override fun configure() {
        bind(UserCtrl::class.java)
        bind(UserService::class.java)
        bind(UserRepo::class.java).to(HibernateUserRepo::class.java)

        bind(MessageCtrl::class.java)
        bind(MessageService::class.java)
        bind(MessageRepo::class.java).to(HibernateMessageRepo::class.java)

        bind(SecurityFilter::class.java)
    }

    @Provides
    fun provideEntityManager(): EntityManagerFactory {
        val dataSource = DataSourceBuilder()
        dataSource.jdbcDriver = appConfig.get(jdbc.driver)
        dataSource.jdbcUrl = appConfig.get(jdbc.url)
        dataSource.jdbcUser = appConfig.get(jdbc.user)
        dataSource.jdbcPass = appConfig.get(jdbc.pass)
        dataSource.entityManagerFactory()

        return dataSource.entityManagerFactory()
    }

    @Provides
    fun provideConfiguration(): Configuration {
        return appConfig
    }

}
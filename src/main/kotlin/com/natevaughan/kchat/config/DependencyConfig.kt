package com.natevaughan.kchat.config

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.natevaughan.kchat.ChatCtrl
import com.natevaughan.kchat.ChatService
import com.natevaughan.kchat.api.ChatRepo
import com.natevaughan.kchat.api.ConnectionPool
import com.natevaughan.kchat.api.MessageRepo
import com.natevaughan.kchat.api.UserRepo
import com.natevaughan.kchat.domain.HikariConnectionPool
import com.natevaughan.kchat.domain.JooqChatRepo
import com.natevaughan.kchat.domain.JooqMessageRepo
import com.natevaughan.kchat.domain.JooqUserRepo
import com.natevaughan.kchat.message.MessageCtrl
import com.natevaughan.kchat.message.MessageService
import com.natevaughan.kchat.user.UserCtrl
import com.natevaughan.kchat.user.UserService
import com.natpryce.konfig.Configuration

/**
 * Created by nate on 11/26/17
 */
class ServiceModule(val appConfig: Configuration) : AbstractModule() {

    override fun configure() {
        bind(UserCtrl::class.java)
        bind(UserService::class.java)
        bind(UserRepo::class.java).to(JooqUserRepo::class.java)

        bind(MessageCtrl::class.java)
        bind(MessageService::class.java)
        bind(MessageRepo::class.java).to(JooqMessageRepo::class.java)

        bind(ChatCtrl::class.java)
        bind(ChatService::class.java)
        bind(ChatRepo::class.java).to(JooqChatRepo::class.java)

        bind(SecurityFilter::class.java)
    }

    @Provides
    fun provideConnectionPool(): ConnectionPool {
        return HikariConnectionPool(appConfig.get(jdbc.url), appConfig.get(jdbc.user), appConfig.get(jdbc.pass))
    }

    @Provides
    fun provideConfiguration(): Configuration {
        return appConfig
    }

}
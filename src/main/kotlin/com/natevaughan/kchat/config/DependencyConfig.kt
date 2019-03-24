package com.natevaughan.kchat.config

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.natevaughan.kchat.api.ChatRepo
import com.natevaughan.kchat.api.ConnectionPool
import com.natevaughan.kchat.api.InviteRepo
import com.natevaughan.kchat.api.MessageRepo
import com.natevaughan.kchat.api.SpaceRepo
import com.natevaughan.kchat.api.UserRepo
import com.natevaughan.kchat.domain.JooqChatRepo
import com.natevaughan.kchat.domain.JooqInviteRepo
import com.natevaughan.kchat.domain.JooqMessageRepo
import com.natevaughan.kchat.domain.JooqSpaceRepo
import com.natevaughan.kchat.domain.JooqUserRepo
import com.natevaughan.kchat.framework.HikariConnectionPool
import com.natevaughan.kchat.rest.InviteCtrl
import com.natevaughan.kchat.rest.MessageCtrl
import com.natevaughan.kchat.rest.SecurityFilter
import com.natevaughan.kchat.rest.SpaceCtrl
import com.natevaughan.kchat.rest.UserCtrl
import com.natevaughan.kchat.service.ChatService
import com.natevaughan.kchat.service.DefaultChatService
import com.natevaughan.kchat.service.DefaultInviteService
import com.natevaughan.kchat.service.DefaultMessageService
import com.natevaughan.kchat.service.DefaultSpaceService
import com.natevaughan.kchat.service.DefaultUserService
import com.natevaughan.kchat.service.InviteService
import com.natevaughan.kchat.service.MessageService
import com.natevaughan.kchat.service.SpaceService
import com.natevaughan.kchat.service.UserService
import com.natpryce.konfig.Configuration
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

/**
 * Created by nate on 11/26/17
 */
class ServiceModule(val appConfig: Configuration) : AbstractModule() {

	override fun configure() {
		bind(UserCtrl::class.java)
		bind(UserService::class.java).to(DefaultUserService::class.java)
		bind(UserRepo::class.java).to(JooqUserRepo::class.java)

		bind(MessageCtrl::class.java)
		bind(MessageService::class.java).to(DefaultMessageService::class.java)
		bind(MessageRepo::class.java).to(JooqMessageRepo::class.java)

		bind(ChatService::class.java).to(DefaultChatService::class.java)
		bind(ChatRepo::class.java).to(JooqChatRepo::class.java)

		bind(InviteCtrl::class.java)
		bind(InviteService::class.java).to(DefaultInviteService::class.java)
		bind(InviteRepo::class.java).to(JooqInviteRepo::class.java)

		bind(SpaceCtrl::class.java)
		bind(SpaceService::class.java).to(DefaultSpaceService::class.java)
		bind(SpaceRepo::class.java).to(JooqSpaceRepo::class.java)

		bind(SecurityFilter::class.java)

		bind(ConnectionPool::class.java).to(HikariConnectionPool::class.java)
	}

	@Provides
	fun provideConfiguration(): Configuration {
		return appConfig
	}

	@Provides
	fun provideHikariDataSource(): HikariDataSource {
		val config = HikariConfig()
		config.jdbcUrl = appConfig.get(jdbc.url)
		config.username = appConfig.get(jdbc.user)
		config.password = appConfig.get(jdbc.pass)
		config.addDataSourceProperty("cachePrepStmts", "true")
		config.addDataSourceProperty("prepStmtCacheSize", "250")
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
		config.addDataSourceProperty("maximumPoolSize","16")

		return HikariDataSource(config)
	}
}
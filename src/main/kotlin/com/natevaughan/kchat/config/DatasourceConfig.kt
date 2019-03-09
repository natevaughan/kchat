package com.natevaughan.kchat.config

import jersey.repackaged.com.google.common.collect.ImmutableMap
import org.hibernate.cfg.AvailableSettings.DIALECT
import org.hibernate.cfg.AvailableSettings.GENERATE_STATISTICS
import org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO
import org.hibernate.cfg.AvailableSettings.QUERY_STARTUP_CHECKING
import org.hibernate.cfg.AvailableSettings.SHOW_SQL
import org.hibernate.cfg.AvailableSettings.STATEMENT_BATCH_SIZE
import org.hibernate.cfg.AvailableSettings.USE_QUERY_CACHE
import org.hibernate.cfg.AvailableSettings.USE_REFLECTION_OPTIMIZER
import org.hibernate.cfg.AvailableSettings.USE_SECOND_LEVEL_CACHE
import org.hibernate.cfg.AvailableSettings.USE_STRUCTURED_CACHE
import org.hibernate.dialect.MySQL57Dialect
import org.hibernate.jpa.HibernatePersistenceProvider
import org.hibernate.tool.schema.Action
import java.io.IOException
import java.io.UncheckedIOException
import java.net.URL
import java.util.*
import javax.persistence.EntityManagerFactory
import javax.persistence.SharedCacheMode
import javax.persistence.ValidationMode
import javax.persistence.spi.ClassTransformer
import javax.persistence.spi.PersistenceUnitInfo
import javax.persistence.spi.PersistenceUnitTransactionType
import javax.sql.DataSource


/**
 * Created by nate on 11/23/17
 */
class DataSourceBuilder : ImmutableMap.Builder<String, Any>() {

    init {
        put(DIALECT, MySQL57Dialect::class.java)
        put(SHOW_SQL, false)
        put(HBM2DDL_AUTO, Action.NONE)
        put(QUERY_STARTUP_CHECKING, true)
        put(GENERATE_STATISTICS, false)
        put(USE_REFLECTION_OPTIMIZER, false)
        put(USE_SECOND_LEVEL_CACHE, false)
        put(USE_QUERY_CACHE, false)
        put(USE_STRUCTURED_CACHE, false)
        put(STATEMENT_BATCH_SIZE, 20)
    }

    fun entityManagerFactory(): EntityManagerFactory {
        return HibernatePersistenceProvider().createContainerEntityManagerFactory(
                ArchiverPersistenceUnitInfo(),
                    build())
    }
}

class ArchiverPersistenceUnitInfo: PersistenceUnitInfo {

    override fun getManagedClassNames(): List<String> {
        return listOf(
            "com.natevaughan.kchat.domain.ChatEntity",
            "com.natevaughan.kchat.domain.MessageEntity",
            "com.natevaughan.kchat.domain.UserEntity"
        )
    }

    override fun getPersistenceUnitName(): String {
        return "KchatPersistence"
    }

    override fun getPersistenceProviderClassName(): String {
        return "org.hibernate.jpa.HibernatePersistenceProvider"
    }

    override fun getTransactionType(): PersistenceUnitTransactionType {
        return PersistenceUnitTransactionType.RESOURCE_LOCAL
    }

    override fun getMappingFileNames(): List<String> {
        return emptyList()
    }

    override fun getJarFileUrls(): List<URL> {
        try {
            return this::class.java.classLoader.getResources("").toList()
        } catch (e: IOException) {
            throw UncheckedIOException(e)
        }
    }

    override fun getProperties(): Properties {
        return Properties()
    }

    override fun getJtaDataSource(): DataSource? {
        return null
    }

    override fun getNonJtaDataSource(): DataSource? {
        return null
    }

    override fun getPersistenceUnitRootUrl(): URL? {
        return null
    }

    override fun excludeUnlistedClasses(): Boolean {
        return false
    }

    override fun getSharedCacheMode(): SharedCacheMode? {
        return null
    }

    override fun getValidationMode(): ValidationMode? {
        return null
    }

    override fun getPersistenceXMLSchemaVersion(): String? {
        return null
    }

    override fun getClassLoader(): ClassLoader? {
        return null
    }

    override fun addTransformer(transformer: ClassTransformer) {

    }

    override fun getNewTempClassLoader(): ClassLoader? {
        return null
    }
}
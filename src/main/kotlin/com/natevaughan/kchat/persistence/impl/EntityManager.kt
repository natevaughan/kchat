package com.natevaughan.kchat.persistence.impl

import com.natevaughan.kchat.DataSourceBuilder
import javax.persistence.FlushModeType

/**
 * Created by nate on 11/23/17
 */

object EntityManagerContainer {
    val entityManager = DataSourceBuilder().entityManagerFactory().createEntityManager()
    init {
        entityManager.flushMode = FlushModeType.AUTO
    }
}

package com.natevaughan.kchat.properties

import com.natpryce.konfig.PropertyGroup
import com.natpryce.konfig.getValue
import com.natpryce.konfig.longType

/**
 * Created by nate on 3/21/19
 */
object invite : PropertyGroup() {
	object expiration: PropertyGroup() {
		val millis by longType
	}
}
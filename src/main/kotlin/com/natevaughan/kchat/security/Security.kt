package com.natevaughan.kchat.security

import com.natevaughan.kchat.model.User
import java.security.Principal
import javax.ws.rs.core.SecurityContext

/**
 * Created by nate on 11/22/17
 */
class  MySecurityContext(val user: User): SecurityContext {

    override fun isUserInRole(role: String?): Boolean {
        return true // check roles if you need ...
    }

    override fun getAuthenticationScheme(): String? {
        return null // ...
    }

    override fun getUserPrincipal(): Principal {
        return user // return your user here - User must implement Principal
    }

    override fun isSecure(): Boolean {
        return false // check HTTPS
    }
}
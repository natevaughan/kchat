package com.natevaughan.kchat.api;

import java.security.Principal;

/**
 * Created by nate on 3/8/19
 */
public interface User extends Principal {
	Long getId();
	Role getRole();
	String getApiKey();

	enum Role { USER, ADMIN }
}

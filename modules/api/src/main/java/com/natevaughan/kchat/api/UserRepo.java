package com.natevaughan.kchat.api;

/**
 * Created by nate on 3/8/19
 */
public interface UserRepo extends Repository<User> {
	User findByApiKey(String key);
}

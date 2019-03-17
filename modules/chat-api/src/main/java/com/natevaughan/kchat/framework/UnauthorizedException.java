package com.natevaughan.kchat.framework;

/**
 * Created by nate on 3/8/19
 */
public class UnauthorizedException extends Exception {
	public UnauthorizedException(String message) {
		super(message);
	}
}

package com.natevaughan.kchat.framework;

/**
 * Created by nate on 3/8/19
 */
public class NotFoundException extends Exception {
	public NotFoundException(String message) {
		super(message);
	}
	public NotFoundException() {
		super();
	}
}

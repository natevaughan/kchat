package com.natevaughan.kchat.api;

/**
 * Created by nate on 3/8/19
 */
public interface Repository<T> {
	T findById(Long id);
	T save(T entity);
	Boolean delete(T entity);
}

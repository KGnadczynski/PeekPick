package com.tackpad.dao;

public interface BaseDao<T> {
	void save(T t);
	T findById(Long id);
}

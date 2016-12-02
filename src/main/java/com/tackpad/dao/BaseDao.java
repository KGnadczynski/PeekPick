package com.tackpad.dao;

public interface BaseDao<T> {
	void save(T t);
	void merge(T t);
	void delete(T t);
	T findById(Long id);
}

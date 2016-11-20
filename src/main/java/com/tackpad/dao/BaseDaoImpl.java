package com.tackpad.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
@Transactional
public class BaseDaoImpl<T> implements BaseDao<T> {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public void save(T t)  {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(t);
}

	@Override
	public T findById(Long id) {
		return null;
	}
}

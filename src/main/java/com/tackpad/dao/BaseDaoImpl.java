package com.tackpad.dao;

import com.tackpad.models.CompanyBranch;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.lang.reflect.ParameterizedType;


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
	public void merge(T t) {
		Session session = sessionFactory.getCurrentSession();
		session.merge(t);
	}

	@Override
	public void delete(T t) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(t);
	}

	@Override
	public T findById(Long id) {
		Session session = sessionFactory.getCurrentSession();
		return session.get((Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0], id);
	}
}

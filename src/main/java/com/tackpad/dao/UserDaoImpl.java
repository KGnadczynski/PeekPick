package com.tackpad.dao;

import com.tackpad.models.Company;
import com.tackpad.models.oauth2.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

	@Autowired
	SessionFactory sessionFactory;


	@Override
	public List<User> getPage(int page, int pageSize) {
		return null;
	}

	@Override
	public User findByEmail(String email) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("login", email));
		return (User) criteria.uniqueResult();
	}
}

package com.tackpad.dao;

import com.tackpad.models.enums.UserStatus;
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
	public User findByEmail(String email) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("login", email));
		criteria.add(Restrictions.not(Restrictions.eq("status", UserStatus.DELETED)));
		return (User) criteria.uniqueResult();
	}

	@Override
	public User findByPhoneNumber(String phoneNumber) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("phoneNumber", phoneNumber));
		criteria.add(Restrictions.not(Restrictions.eq("status", UserStatus.DELETED)));
		return (User) criteria.uniqueResult();
	}

	@Override
	public List<User> getPage(int page, int pageSize, String searchTerm) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(User.class);
		criteria.setMaxResults(pageSize);
		criteria.setFirstResult(pageSize * page);
		if (searchTerm != null) {
			criteria.add(Restrictions.or(Restrictions.ilike("login", "%" + searchTerm+ "%"), Restrictions.ilike("name", "%" + searchTerm+ "%")));
		}
		return criteria.list();
	}
}

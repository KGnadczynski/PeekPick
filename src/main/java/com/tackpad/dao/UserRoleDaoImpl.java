package com.tackpad.dao;

import com.tackpad.models.enums.UserRoleType;
import com.tackpad.models.oauth2.User;
import com.tackpad.models.oauth2.UserRole;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class UserRoleDaoImpl extends BaseDaoImpl<UserRole> implements UserRoleDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public UserRole findByUserRole(UserRoleType userRoleType) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserRole.class);
		criteria.add(Restrictions.eq("name", userRoleType));
		return (UserRole) criteria.uniqueResult();
	}
}

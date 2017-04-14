package com.tackpad.dao;

import com.tackpad.models.UserDeviceFCMToken;
import com.tackpad.models.UserNotification;
import com.tackpad.models.enums.UserNotificationStatus;
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
public class UserNotificationDaoImpl extends BaseDaoImpl<UserNotification> implements UserNotificationDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<UserNotification> findListByStatus(UserNotificationStatus status) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserNotification.class, "m");
		criteria.add(Restrictions.lt("status", status));
		return criteria.list();
	}
}

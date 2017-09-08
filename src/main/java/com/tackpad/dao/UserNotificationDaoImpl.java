package com.tackpad.dao;

import com.tackpad.models.UserNotification;
import com.tackpad.models.enums.UserNotificationStatus;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
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
		criteria.add(Restrictions.eq("status", status));
		return criteria.list();
	}

	@Override
	public List<UserNotification> getPage(int page, int pageSize, String searchTerm, Long userId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserNotification.class);
		criteria.setMaxResults(pageSize);
		criteria.setFirstResult(pageSize * page);
		if (searchTerm != null) {
			criteria.add(Restrictions.or(Restrictions.ilike("content", "%" + searchTerm+ "%"), Restrictions.ilike("title", "%" + searchTerm+ "%")));
		}

		if (userId != null) {
			criteria.add(Restrictions.eq("user.id", userId));
		}

		criteria.add(Restrictions.not(Restrictions.eq("status", UserNotificationStatus.DELETED)));
		criteria.addOrder(Order.desc("createDate"));
		return criteria.list();
	}

	@Override
	public int gatCountByStatus(UserNotificationStatus status, Long userId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserNotification.class, "m");
		criteria.add(Restrictions.eq("status", status));
		criteria.add(Restrictions.eq("user.id", userId));
		return ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
}

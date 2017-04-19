package com.tackpad.dao;

import com.tackpad.models.Message;
import com.tackpad.models.UserDeviceFCMToken;
import com.tackpad.models.UserNotification;
import com.tackpad.models.enums.DeviceType;
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
public class UserDeviceFCMTokenImpl extends BaseDaoImpl<UserDeviceFCMToken> implements UserDeviceFCMTokenDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<UserDeviceFCMToken> findByUserId(Long userId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDeviceFCMToken.class, "m");
		criteria.add(Restrictions.eq("m.user.id", userId));
		return criteria.list();
	}

	@Override
	public UserDeviceFCMToken getByUserIdAndDeviceType(Long userId, DeviceType deviceType) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDeviceFCMToken.class, "m");
		criteria.add(Restrictions.eq("m.user.id", userId));
		criteria.add(Restrictions.eq("m.deviceType", deviceType));
		return (UserDeviceFCMToken) criteria.uniqueResult();
	}
}

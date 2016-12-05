package com.tackpad.dao;


import com.tackpad.models.CompanyBranch;
import com.tackpad.models.MessageImage;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class MessageImageImpl extends BaseDaoImpl<MessageImage> implements MessageImageDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public MessageImage findByMessageId(Long messageId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(MessageImage.class);
		criteria.add(Restrictions.eq("message.id", messageId));
		return (MessageImage) criteria.uniqueResult();
	}
}

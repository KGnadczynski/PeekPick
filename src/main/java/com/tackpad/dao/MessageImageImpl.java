package com.tackpad.dao;


import com.tackpad.models.Image;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class MessageImageImpl extends BaseDaoImpl<Image> implements MessageImageDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Image findByMessageId(Long messageId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Image.class);
		criteria.add(Restrictions.eq("message.id", messageId));
		return (Image) criteria.uniqueResult();
	}

	@Override
	public Image findByCompanyId(Long companyId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Image.class);
		criteria.add(Restrictions.eq("company.id", companyId));
		return (Image) criteria.uniqueResult();
	}
}

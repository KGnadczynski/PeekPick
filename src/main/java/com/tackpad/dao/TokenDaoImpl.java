package com.tackpad.dao;


import com.tackpad.models.CompanyBranch;
import com.tackpad.models.Token;
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
public class TokenDaoImpl extends BaseDaoImpl<Token> implements TokenDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Token findByValue(String value) {
		Session session = sessionFactory.getCurrentSession();
		final Criteria criteria = session.createCriteria(Token.class);
		criteria.add(Restrictions.eq("value", value));
		return (Token) criteria.uniqueResult();
	}

}

package com.tackpad.dao;

import com.tackpad.models.Company;
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
public class CompanyDaoImpl extends BaseDaoImpl<Company> implements CompanyDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<Company> getPage(int page, int pageSize, String searchTerm) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Company.class);
		criteria.setMaxResults(pageSize);
		criteria.setFirstResult(pageSize * page);
		if (searchTerm != null) {
			criteria.add(Restrictions.ilike("name", "%" + searchTerm + "%"));
		}
		return criteria.list();
	}

	@Override
	public Company findById(Long id) {
		Session session = sessionFactory.getCurrentSession();
		return (Company) session.get(Company.class, id);
	}

	@Override
	public Company findByName(String name) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Company.class);
		criteria.add(Restrictions.eq("content", name));
		return (Company) criteria.uniqueResult();
	}

}

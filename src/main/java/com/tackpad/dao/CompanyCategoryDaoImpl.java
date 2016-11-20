package com.tackpad.dao;

import com.tackpad.models.CompanyCategory;
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
public class CompanyCategoryDaoImpl extends BaseDaoImpl<CompanyCategory> implements CompanyCategoryDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<CompanyCategory> findListEmptyParentCategory() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CompanyCategory.class);
		criteria.add(Restrictions.isNull("parentCategory.id"));
		return criteria.list();
	}

	@Override
	public List<CompanyCategory> findListByParentCategory(Long parentCategoryId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CompanyCategory.class);
		criteria.add(Restrictions.eq("parentCategory.id", parentCategoryId));
		return criteria.list();
	}

	@Override
	public CompanyCategory findByName(String name) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CompanyCategory.class);
		criteria.add(Restrictions.eq("name", name));
		return (CompanyCategory) criteria.uniqueResult();
	}

	@Override
	public CompanyCategory findById(Long id) {
		Session session = sessionFactory.getCurrentSession();
		return (CompanyCategory) session.get(CompanyCategory.class, id);
	}
}

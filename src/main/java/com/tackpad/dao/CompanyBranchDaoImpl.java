package com.tackpad.dao;


import com.tackpad.models.Company;
import com.tackpad.models.CompanyBranch;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CompanyBranchDaoImpl extends BaseDaoImpl<CompanyBranch> implements CompanyBranchDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<CompanyBranch> getPage(int page, int pageSize) {
		Session session = sessionFactory.getCurrentSession();
		final Criteria criteria = session.createCriteria(CompanyBranch.class);
		criteria.setMaxResults(pageSize);
		criteria.setFirstResult(pageSize * page);
		return criteria.list();
	}

	@Override
	public Company findById() {
		return null;
	}

	@Override
	public CompanyBranch findById(Long id) {
		Session session = sessionFactory.getCurrentSession();
		return (CompanyBranch) session.get(CompanyBranch.class, id);
	}
}

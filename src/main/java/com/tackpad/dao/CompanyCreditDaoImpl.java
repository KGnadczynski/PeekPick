package com.tackpad.dao;


import com.tackpad.models.CompanyCredit;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CompanyCreditDaoImpl extends BaseDaoImpl<CompanyCredit> implements CompanyCreditDao {

    @Override
    public CompanyCredit findByCompanyId(Long companyId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(CompanyCredit.class);
        criteria.add(Restrictions.eq("company.id", companyId));
        return (CompanyCredit) criteria.uniqueResult();
    }

    @Override
    public List<CompanyCredit> findListByCreditAndLastUpdateForFreePlanDateAfter(int availableMessageCount, DateTime lastUpdateForFreePlanDate) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(CompanyCredit.class);
        criteria.add(Restrictions.eq("credit", availableMessageCount));
        criteria.add(Restrictions.lt("lastUpdateForFreePlanDate", lastUpdateForFreePlanDate.toDate()));
        return (List<CompanyCredit>) criteria.list();
    }
}

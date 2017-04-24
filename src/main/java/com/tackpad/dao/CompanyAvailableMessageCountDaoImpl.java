package com.tackpad.dao;


import com.tackpad.models.CompanyAvailableMessageCount;
import com.tackpad.models.Image;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CompanyAvailableMessageCountDaoImpl extends BaseDaoImpl<CompanyAvailableMessageCount> implements CompanyAvailableMessageCountDao {

    @Override
    public CompanyAvailableMessageCount findByCompanyId(Long companyId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(CompanyAvailableMessageCount.class);
        criteria.add(Restrictions.eq("company.id", companyId));
        return (CompanyAvailableMessageCount) criteria.uniqueResult();
    }

    @Override
    public List<CompanyAvailableMessageCount> findListByAvailableMessageCountAndLastUpdateForFreePlanDateAfter(int availableMessageCount, DateTime lastUpdateForFreePlanDate) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(CompanyAvailableMessageCount.class);
        criteria.add(Restrictions.eq("availableMessageCount", availableMessageCount));
        criteria.add(Restrictions.lt("lastUpdateForFreePlanDate", lastUpdateForFreePlanDate.toDate()));
        return (List<CompanyAvailableMessageCount>) criteria.list();
    }
}

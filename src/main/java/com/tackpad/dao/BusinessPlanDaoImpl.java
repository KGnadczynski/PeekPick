package com.tackpad.dao;


import com.tackpad.models.BusinessPlan;
import com.tackpad.models.enums.BusinessPlanStatus;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class BusinessPlanDaoImpl extends BaseDaoImpl<BusinessPlan> implements BusinessPlanDao {

    @Override
    public List<BusinessPlan> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(BusinessPlan.class, "m");
        criteria.add(Restrictions.not(Restrictions.eq("status", BusinessPlanStatus.DELETED)));
        return criteria.list();
    }

}

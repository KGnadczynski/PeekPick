package com.tackpad.services;


import com.tackpad.dao.BusinessPlanDao;
import com.tackpad.dao.CompanyAvailableMessageCountDao;
import com.tackpad.models.BusinessPlan;
import com.tackpad.models.CompanyAvailableMessageCount;
import com.tackpad.models.oauth2.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Company service.
 * @author Przemysaw Zynis
 */
@Component
public class BusinessPlanService extends BaseService {

    @Autowired
    public BusinessPlanDao businessPlanDao;

    /** Save.*/
    public void save(BusinessPlan businessPlan) {
        businessPlanDao.save(businessPlan);
    }

    public List<BusinessPlan> getAll() {
        return businessPlanDao.findAll();
    }

    public BusinessPlan getById(Long messageId) {
        return businessPlanDao.findById(messageId);
    }
}

package com.tackpad.dao;


import com.tackpad.models.BusinessPlan;
import com.tackpad.models.CompanyAvailableMessageCount;

import java.util.List;

public interface BusinessPlanDao extends BaseDao<BusinessPlan> {

    List<BusinessPlan> findAll();
}

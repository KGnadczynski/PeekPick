package com.tackpad.dao;


import com.tackpad.models.BusinessPlan;

import java.util.List;

public interface BusinessPlanDao extends BaseDao<BusinessPlan> {

    List<BusinessPlan> findAll();
}

package com.tackpad.dao;


import com.tackpad.models.Company;
import com.tackpad.models.CompanyAvailableMessageCount;
import org.joda.time.DateTime;

import java.util.List;

public interface CompanyAvailableMessageCountDao extends BaseDao<CompanyAvailableMessageCount> {

    CompanyAvailableMessageCount findByCompanyId(Long companyId);

    List<CompanyAvailableMessageCount> findListByAvailableMessageCountAndLastUpdateForFreePlanDateAfter(int availableMessageCount, DateTime lastUpdateForFreePlanDate);
}

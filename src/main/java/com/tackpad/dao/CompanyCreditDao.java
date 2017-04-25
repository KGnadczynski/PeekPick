package com.tackpad.dao;


import com.tackpad.models.CompanyCredit;
import org.joda.time.DateTime;

import java.util.List;

public interface CompanyCreditDao extends BaseDao<CompanyCredit> {

    CompanyCredit findByCompanyId(Long companyId);

    List<CompanyCredit> findListByCreditAndLastUpdateForFreePlanDateAfter(int availableMessageCount, DateTime lastUpdateForFreePlanDate);
}

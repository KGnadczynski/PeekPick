package com.tackpad.services;


import com.tackpad.dao.CompanyCreditDao;
import com.tackpad.models.CompanyCredit;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Company service.
 * @author Przemysaw Zynis
 */
@Component
public class CompanyCreditService extends BaseService {

    @Autowired
    public CompanyCreditDao companyCreditDao;

    /** Save.*/
    public void save(CompanyCredit companyCredit) {
        companyCreditDao.save(companyCredit);
    }


    public CompanyCredit getByCompanyId(Long companyId) {
        return companyCreditDao.findByCompanyId(companyId);
    }

    public void merge(CompanyCredit companyCredit) {
        companyCreditDao.merge(companyCredit);
    }

    public List<CompanyCredit> getListByAvailableMessageCountAndLastUpdateForFreePlanDateAfter(int credit, DateTime lastUpdateForFreePlanDate) {
       return companyCreditDao.findListByCreditAndLastUpdateForFreePlanDateAfter(credit, lastUpdateForFreePlanDate);
    }
}

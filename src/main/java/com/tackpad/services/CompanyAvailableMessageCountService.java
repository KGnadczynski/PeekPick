package com.tackpad.services;


import com.tackpad.dao.CompanyAvailableMessageCountDao;
import com.tackpad.models.CompanyAvailableMessageCount;
import com.tackpad.models.Message;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Company service.
 * @author Przemysaw Zynis
 */
@Component
public class CompanyAvailableMessageCountService extends BaseService {

    @Autowired
    public CompanyAvailableMessageCountDao companyAvailableMessageCountDao;

    /** Save.*/
    public void save(CompanyAvailableMessageCount companyAvailableMessageCount) {
        companyAvailableMessageCountDao.save(companyAvailableMessageCount);
    }


    public CompanyAvailableMessageCount getByCompanyId(Long companyId) {
        return companyAvailableMessageCountDao.findByCompanyId(companyId);
    }

    public void merge(CompanyAvailableMessageCount companyAvailableMessageCount) {
        companyAvailableMessageCountDao.merge(companyAvailableMessageCount);
    }

    public List<CompanyAvailableMessageCount> getListByAvailableMessageCountAndLastUpdateForFreePlanDateAfter(int availableMessageCount, DateTime lastUpdateForFreePlanDate) {
       return companyAvailableMessageCountDao.findListByAvailableMessageCountAndLastUpdateForFreePlanDateAfter(availableMessageCount, lastUpdateForFreePlanDate);
    }
}

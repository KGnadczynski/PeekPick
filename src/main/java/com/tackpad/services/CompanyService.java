package com.tackpad.services;


import com.tackpad.dao.CompanyDao;
import com.tackpad.models.Company;
import com.tackpad.responses.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Company service.
 * @author Przemysaw Zynis
 */
@Component
public class CompanyService extends BaseService {

    @Autowired
    public CompanyDao companyDao;

    /**
     *
     * retrieve a window of accounts
     *
     * @return List of Accounts
     */
    public Page<Company> getPage(Integer pageNum, Integer pageSize) {

        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }

        Page<Company> response = new Page<>();
        response.objectList = companyDao.getPage(pageNum - 1, pageSize);
        return response;
    }

    /** Save.*/
    public void save(Company company) {
        companyDao.save(company);
    }


}

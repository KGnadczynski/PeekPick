package com.tackpad.services;


import com.tackpad.dao.CompanyDao;
import com.tackpad.models.Company;
import com.tackpad.responses.CompanyPage;
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
    public CompanyPage getPage(Integer pageNum, Integer pageSize) {

        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }

        CompanyPage response = new CompanyPage();
        response.objectList = companyDao.getPage(pageNum - 1, pageSize);
        return response;
    }

    public Company getById(Long id) {
        return companyDao.findById(id);
    }

    /** Save.*/
    public void save(Company company) {
        companyDao.save(company);
    }


}

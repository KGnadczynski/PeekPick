package com.tackpad.services;


import com.tackpad.dao.CompanyBranchDao;
import com.tackpad.models.CompanyBranch;
import com.tackpad.responses.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * CompanyBranch service.
 * @author Przemysaw Zynis
 */
@Component
public class CompanyBranchService extends BaseService {

    @Autowired
    public CompanyBranchDao companyBranchDao;

    /**
     *
     * retrieve a window of accounts
     *
     * @return List of Accounts
     */
    public Page<CompanyBranch> getPage(Integer pageNum, Integer pageSize) {

        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }

        Page<CompanyBranch> response = new Page<>();
        response.objectList = companyBranchDao.getPage(pageNum - 1, pageSize);
        return response;
    }

    /**
     *
     * Pobieranie po id
     *
     * @return lista głównych kategorii
     */
    public CompanyBranch getById(Long id) {

        return companyBranchDao.findById(id);
    }

    /**
     *
     * Pobieranie po id
     *
     * @return lista głównych kategorii
     */
    public List<CompanyBranch> getListByCompanyId(Long id) {

        return companyBranchDao.findListByCompanyId(id);
    }


    /** Save.*/
    public void save(CompanyBranch companyBranch) {
        companyBranchDao.save(companyBranch);
    }

}

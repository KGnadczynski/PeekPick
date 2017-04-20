package com.tackpad.services;


import com.tackpad.dao.CompanyBranchDao;
import com.tackpad.models.CompanyBranch;
import com.tackpad.models.Message;
import com.tackpad.requests.enums.ListingSortType;
import com.tackpad.responses.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.text.ParseException;
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

    @Cacheable(value = "companyBranchPages", cacheManager="timeoutCacheManager")
    public Page<CompanyBranch> getPage(Integer page, Integer pageSize, List<Long> messageIdList, Long companyBranchId, Long companyId, Double latitude,
                                 Double longitude, Double range, String searchTerm, ListingSortType listingSortType) throws ParseException {

        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }

        if (listingSortType == null) {
            listingSortType = ListingSortType.CREATE_DATE;
        }

        Page<CompanyBranch> response = new Page<>();
        response.objectList = companyBranchDao.getPage(page - 1, pageSize, messageIdList,companyBranchId,  companyId, latitude,
                longitude, range, searchTerm, listingSortType);

        if (response.objectList.size() < pageSize) {
            response.isLastPage = true;
        }

        return response;
    }

    public CompanyBranch getMainCompanyBranch(Long companyId) {
        return companyBranchDao.getMainCompanyBranch(companyId);
    }


}

package com.tackpad.services;


import com.tackpad.dao.CompanyCategoryDao;
import com.tackpad.models.CompanyCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Company service.
 * @author Przemysaw Zynis
 */
@Component
public class CompanyCategoryService extends BaseService {

    @Autowired
    public CompanyCategoryDao companyCategoryDao;

    /**
     *
     * Pobieranie listy głównych kategorii.
     *
     * @return lista głównych kategorii
     */
    public List<CompanyCategory> getMainCategoryList() {

        return companyCategoryDao.findListEmptyParentCategory();
    }

    /**
     *
     * Pobieranie listy podkategorii
     *
     * @return lista głównych kategorii
     */
    public List<CompanyCategory> getSubcategoryList(Long patentCategoryId) {

        return companyCategoryDao.findListByParentCategory(patentCategoryId);
    }

    /**
     *
     * Pobieranie po id
     *
     * @return kategoria
     */
    public CompanyCategory getById(Long id) {

        return companyCategoryDao.findById(id);
    }

    /**
     *
     * Pobieranie po nazwie
     *
     * @return  kategoria
     */
    public CompanyCategory getByName(String name) {

        return companyCategoryDao.findByName(name);
    }

    /** Create .*/
    public void save(CompanyCategory category) {
        companyCategoryDao.save(category);
    }

}

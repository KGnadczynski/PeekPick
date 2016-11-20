package com.tackpad.dao;


import com.tackpad.models.CompanyCategory;

import java.util.List;

public interface CompanyCategoryDao extends BaseDao<CompanyCategory> {
	List<CompanyCategory> findListEmptyParentCategory();
	List<CompanyCategory> findListByParentCategory(Long patentCategoryId);
	CompanyCategory findByName(String name);
}

package com.tackpad.dao;


import com.tackpad.models.Company;

import java.util.List;

public interface CompanyDao extends BaseDao<Company> {
	List<Company> getPage(int page, int pageSize, String searchTerm);
	Company findById(Long id);
	Company findByName(String name);
}

package com.tackpad.dao;


import com.tackpad.models.Company;
import com.tackpad.models.CompanyBranch;

import java.util.List;

public interface CompanyBranchDao extends BaseDao<CompanyBranch> {
	List<CompanyBranch> getPage(int page, int pageSize);
	Company findById();
}

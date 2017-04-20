package com.tackpad.dao;


import com.tackpad.models.CompanyBranch;
import com.tackpad.requests.enums.ListingSortType;

import java.text.ParseException;
import java.util.List;

public interface CompanyBranchDao extends BaseDao<CompanyBranch> {
	List<CompanyBranch> getPage(int page, int pageSize);
	List findListByCompanyId(Long companyId);

	List<CompanyBranch> getPage(int page, int pageSize, List<Long> messageIdList, Long companyBranchId, Long companyId, Double latitude,
						  Double longitude, Double range, String searchTerm, ListingSortType listingSortType) throws ParseException;

	CompanyBranch getMainCompanyBranch(Long companyId);
}

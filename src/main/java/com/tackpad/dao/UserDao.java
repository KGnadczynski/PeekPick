package com.tackpad.dao;


import com.tackpad.models.oauth2.User;

import java.util.List;

public interface UserDao extends BaseDao<User> {
	List<User> getPage(int page, int pageSize, String searchTerm);
	User findByEmail(String email);

	User findByPhoneNumber(String phoneNumber);

    List<User> findByCompanyId(Long companyId);
}

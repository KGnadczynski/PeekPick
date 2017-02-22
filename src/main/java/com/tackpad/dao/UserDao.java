package com.tackpad.dao;


import com.tackpad.models.CompanyBranch;
import com.tackpad.models.oauth2.User;

import java.util.List;

public interface UserDao extends BaseDao<User> {
	List<User> getPage(int page, int pageSize);
	User findByEmail(String email);

	User findByPhoneNumber(String phoneNumber);
}

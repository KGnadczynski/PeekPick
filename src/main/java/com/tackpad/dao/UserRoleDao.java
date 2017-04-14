package com.tackpad.dao;


import com.tackpad.models.enums.UserRoleType;
import com.tackpad.models.oauth2.User;
import com.tackpad.models.oauth2.UserRole;

import java.util.List;

public interface UserRoleDao extends BaseDao<UserRole> {

    UserRole findByUserRole(UserRoleType businessUser);
}

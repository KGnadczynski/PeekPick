package com.tackpad.services;

import com.tackpad.dao.UserDao;
import com.tackpad.dao.UserRoleDao;
import com.tackpad.models.enums.UserRoleType;
import com.tackpad.models.oauth2.User;
import com.tackpad.models.oauth2.UserRole;
import com.tackpad.responses.USerPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Company service.
 * @author Przemysaw Zynis
 */
@Component
public class UserRoleService extends BaseService {

    @Autowired
    public UserRoleDao userRoleDao;

    public UserRole getByUserRole(UserRoleType businessUser) {
        return userRoleDao.findByUserRole(businessUser);
    }

}

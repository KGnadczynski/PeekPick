package com.tackpad.services;


import com.tackpad.dao.CompanyDao;
import com.tackpad.dao.UserDao;
import com.tackpad.models.Company;
import com.tackpad.models.oauth2.User;
import com.tackpad.responses.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Company service.
 * @author Przemysaw Zynis
 */
@Component
public class UserService extends BaseService {

    @Autowired
    public UserDao userDao;

    public User getByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public void save(User user) {
        userDao.save(user);
    }

    public void merge(User user) {
        userDao.merge(user);
    }
}

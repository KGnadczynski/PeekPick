package com.tackpad.services;

import com.tackpad.dao.CompanyDao;
import com.tackpad.dao.UserDao;
import com.tackpad.models.Company;
import com.tackpad.models.oauth2.User;
import com.tackpad.responses.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Company service.
 * @author Przemysaw Zynis
 */
@Component
public class UserService extends BaseService {

    @Autowired
    public UserDao userDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User getByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public void save(User user) {
        userDao.save(user);
    }

    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userDao.merge(user);
    }

    public void update(User user) {
        userDao.merge(user);
    }

    public void create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    public void merge(User user) {
        userDao.merge(user);
    }

    public Object getByPhoneNumber(String phoneNumber) {
        return userDao.findByPhoneNumber(phoneNumber);
    }
}

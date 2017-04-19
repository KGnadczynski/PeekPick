package com.tackpad.services;

import com.tackpad.dao.CompanyDao;
import com.tackpad.dao.UserDao;
import com.tackpad.models.Company;
import com.tackpad.models.oauth2.User;
import com.tackpad.responses.CompanyPage;
import com.tackpad.responses.Page;
import com.tackpad.responses.USerPage;
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

    public User findById(Long id) {
        return userDao.findById(id);
    }

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

    public USerPage getPage(Integer pageNum, Integer pageSize, String searchTerm) {

        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }

        USerPage response = new USerPage();
        response.objectList = userDao.getPage(pageNum - 1, pageSize, searchTerm);
        response.isLastPage = response.objectList.size() != pageSize;
        return response;
    }

    public User getById(Long messageId) {
        return userDao.findById(messageId);
    }
}

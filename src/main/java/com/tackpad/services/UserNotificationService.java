package com.tackpad.services;

import com.tackpad.dao.UserNotificationDao;
import com.tackpad.models.UserNotification;
import com.tackpad.models.enums.UserNotificationStatus;
import com.tackpad.responses.USerPage;
import com.tackpad.responses.UserNotificationPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserNotificationService extends BaseService {

    @Autowired
    public UserNotificationDao userNotificationDao;

    public void save(UserNotification user) {
        userNotificationDao.save(user);
    }

    public List<UserNotification> gatListByStatus(UserNotificationStatus status) {
        return userNotificationDao.findListByStatus(status);
    }

    public int gatCountByStatus(UserNotificationStatus status, Long userId) {
        return userNotificationDao.gatCountByStatus(status, userId);
    }

    public UserNotificationPage getPage(Integer pageNum, Integer pageSize, String searchTerm, Long userId) {

        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }

        UserNotificationPage response = new UserNotificationPage();
        response.objectList = userNotificationDao.getPage(pageNum - 1, pageSize, searchTerm, userId);
        response.isLastPage = response.objectList.size() != pageSize;
        return response;
    }

    public UserNotification get(Long userNotificationId) {
        return userNotificationDao.findById(userNotificationId);
    }

}

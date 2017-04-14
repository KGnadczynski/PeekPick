package com.tackpad.services;

import com.tackpad.dao.UserNotificationDao;
import com.tackpad.models.UserNotification;
import com.tackpad.models.enums.UserNotificationStatus;
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
}

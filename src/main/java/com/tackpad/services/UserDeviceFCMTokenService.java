package com.tackpad.services;

import com.tackpad.dao.UserDeviceFCMTokenDao;
import com.tackpad.dao.UserNotificationDao;
import com.tackpad.models.UserDeviceFCMToken;
import com.tackpad.models.UserNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDeviceFCMTokenService extends BaseService {

    @Autowired
    public UserDeviceFCMTokenDao userDeviceFCMTokenDao;

    public void save(UserDeviceFCMToken fcmToken) {
        userDeviceFCMTokenDao.save(fcmToken);
    }

    public List<UserDeviceFCMToken> getByUserId(Long usedId) {
        return userDeviceFCMTokenDao.findByUserId(usedId);
    }
}

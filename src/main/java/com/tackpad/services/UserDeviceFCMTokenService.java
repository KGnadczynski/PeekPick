package com.tackpad.services;

import com.tackpad.dao.UserDeviceFCMTokenDao;
import com.tackpad.dao.UserNotificationDao;
import com.tackpad.models.UserDeviceFCMToken;
import com.tackpad.models.UserNotification;
import com.tackpad.models.enums.DeviceType;
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

    public void delete(UserDeviceFCMToken fcmToken) {
        userDeviceFCMTokenDao.delete(fcmToken);
    }

    public List<UserDeviceFCMToken> getByUserId(Long usedId) {
        return userDeviceFCMTokenDao.findByUserId(usedId);
    }

    public UserDeviceFCMToken getByUserIdAndDeviceType(Long userId, DeviceType deviceType) {
        return userDeviceFCMTokenDao.getByUserIdAndDeviceType(userId, deviceType);
    }
}

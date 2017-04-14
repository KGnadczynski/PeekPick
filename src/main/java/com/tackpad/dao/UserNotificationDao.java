package com.tackpad.dao;

import com.tackpad.models.UserNotification;
import com.tackpad.models.enums.UserNotificationStatus;

import java.util.List;

public interface UserNotificationDao extends BaseDao<UserNotification> {

    List findListByStatus(UserNotificationStatus status);
}


package com.tackpad.dao;

import com.tackpad.models.UserNotification;
import com.tackpad.models.enums.UserNotificationStatus;

import java.util.List;

public interface UserNotificationDao extends BaseDao<UserNotification> {

    List<UserNotification>  findListByStatus(UserNotificationStatus status);

    List<UserNotification> getPage(int page, int pageSize, String searchTerm, Long userId);
}


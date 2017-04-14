package com.tackpad.dao;

import com.tackpad.models.UserDeviceFCMToken;
import com.tackpad.models.UserNotification;
import com.tackpad.models.enums.UserRoleType;
import com.tackpad.models.oauth2.UserRole;

import java.util.List;

public interface UserDeviceFCMTokenDao extends BaseDao<UserDeviceFCMToken> {
    List<UserDeviceFCMToken> findByUserId(Long userId);
}

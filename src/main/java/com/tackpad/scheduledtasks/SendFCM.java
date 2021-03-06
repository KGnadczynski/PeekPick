package com.tackpad.scheduledtasks;

import com.tackpad.models.UserDeviceFCMToken;
import com.tackpad.models.UserNotification;
import com.tackpad.models.enums.UserNotificationStatus;
import com.tackpad.services.SendFCMRequestsService;
import com.tackpad.services.UserDeviceFCMTokenService;
import com.tackpad.services.UserNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SendFCM {

    private static final Logger log = LoggerFactory.getLogger(RemoveEndedPosts.class);

    @Autowired
    UserNotificationService userNotificationService;

    @Autowired
    public UserDeviceFCMTokenService userDeviceFCMTokenService;

    @Autowired
    public SendFCMRequestsService sendFCMRequestsService;

    @Scheduled(fixedRate = 30000)
    public void sendFCM() {
        log.info(" START SEND FCM MESSAGES");
        List<UserNotification> userNotificationList = userNotificationService.gatListByStatus(UserNotificationStatus.NOT_SEND);
        log.info(" MESSAGES COUNT {}: " + userNotificationList.size());
        for (UserNotification userNotification : userNotificationList) {
            List<UserDeviceFCMToken> userDeviceFCMTokenList = userDeviceFCMTokenService.getByUserId(userNotification.getUser().getId());
            for (UserDeviceFCMToken deviceFCMToken : userDeviceFCMTokenList) {
                sendFCMRequestsService.sendPushMessages(deviceFCMToken.getToken(), userNotification);
            }

        }

    }
}

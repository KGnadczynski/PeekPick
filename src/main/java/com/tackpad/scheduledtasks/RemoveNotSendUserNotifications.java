package com.tackpad.scheduledtasks;

import com.tackpad.models.UserDeviceFCMToken;
import com.tackpad.models.UserNotification;
import com.tackpad.models.enums.UserNotificationStatus;
import com.tackpad.services.SendFCMRequestsService;
import com.tackpad.services.UserDeviceFCMTokenService;
import com.tackpad.services.UserNotificationService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RemoveNotSendUserNotifications {

    private static final Logger log = LoggerFactory.getLogger(RemoveEndedPosts.class);

    private static final int EXPIRED_DAY_COUNT = 3;

    @Autowired
    UserNotificationService userNotificationService;

    //3h
    @Scheduled(fixedRate = 3*60*60*1000 )
    public void sendFCM() {
        log.info(" ADD FREE PLANS TASK");
        List<UserNotification> userNotificationList = userNotificationService.gatListByStatus(UserNotificationStatus.NOT_SEND);
        for (UserNotification userNotification : userNotificationList) {
            if (userNotification.getCreateDate().after(DateTime.now().plusDays(EXPIRED_DAY_COUNT).toDate())) {
                userNotification.setStatus(UserNotificationStatus.WAS_SEND);
                userNotificationService.save(userNotification);
            }
        }
    }
}

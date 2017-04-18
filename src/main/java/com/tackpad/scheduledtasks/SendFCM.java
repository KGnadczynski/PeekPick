package com.tackpad.scheduledtasks;

import com.tackpad.models.Message;
import com.tackpad.models.UserDeviceFCMToken;
import com.tackpad.models.UserNotification;
import com.tackpad.models.enums.MessageStatus;
import com.tackpad.models.enums.UserNotificationStatus;
import com.tackpad.services.MessageService;
import com.tackpad.services.SendFCMRequestsService;
import com.tackpad.services.UserDeviceFCMTokenService;
import com.tackpad.services.UserNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class SendFCM {

    private static final Logger log = LoggerFactory.getLogger(SendFCM.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    UserNotificationService userNotificationService;

    @Autowired
    public UserDeviceFCMTokenService userDeviceFCMTokenService;

    @Autowired
    public SendFCMRequestsService sendFCMRequestsService;

    @Scheduled(fixedRate = 60  * 60 * 60)
    public void sendFCM() {
        List<UserNotification> userNotificationList = userNotificationService.gatListByStatus(UserNotificationStatus.NOT_SEND);
        for (UserNotification userNotification : userNotificationList) {
            List<UserDeviceFCMToken> userDeviceFCMTokenList = userDeviceFCMTokenService.getByUserId(userNotification.getUser().getId());
            for (UserDeviceFCMToken deviceFCMToken : userDeviceFCMTokenList) {
                sendFCMRequestsService.sendPushMessages(deviceFCMToken.getToken(), userNotification);
            }

        }

    }
}

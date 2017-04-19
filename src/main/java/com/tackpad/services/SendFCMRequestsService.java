package com.tackpad.services;

import com.tackpad.models.UserNotification;
import com.tackpad.models.enums.UserNotificationStatus;
import com.tackpad.scheduledtasks.RemoveEndedPosts;
import de.bytefish.fcmjava.http.client.IFcmClient;
import de.bytefish.fcmjava.model.options.FcmMessageOptions;
import de.bytefish.fcmjava.requests.data.DataUnicastMessage;
import de.bytefish.fcmjava.requests.notification.NotificationPayload;
import de.bytefish.fcmjava.responses.FcmMessageResponse;
import de.bytefish.fcmjava.responses.FcmMessageResultItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class SendFCMRequestsService {

    private static final Logger log = LoggerFactory.getLogger(RemoveEndedPosts.class);

    @Autowired
    private IFcmClient fcmClient;

    @Autowired
    private UserNotificationService userNotificationService;

    private int id = 0;

    public SendFCMRequestsService() {}

    public void sendPushMessages(String deviceFCMTokenToken, UserNotification userNotification) {
        FcmMessageOptions options = FcmMessageOptions.builder()
                .setTimeToLive(Duration.ofMinutes(2)).build();

        NotificationPayload payload = NotificationPayload.builder()
                .setBody(userNotification.getContent())
                .setTitle(userNotification.getTitle())
                .setTag(userNotification.getType().toString())
                .build();

        log.info("Sending personal message to: " + deviceFCMTokenToken + " with id " + userNotification.getUser().getId());
        Map<String, Object> data = new HashMap<>();
        data.put("id", ++this.id);
        data.put("text", Math.random() * 1000);

        DataUnicastMessage message = new DataUnicastMessage(options, deviceFCMTokenToken, data, payload);
        FcmMessageResponse response = this.fcmClient.send(message);
        for (FcmMessageResultItem result : response.getResults()) {
            if (result.getErrorCode() != null) {
                log.info("Sending to %s failed. Error Code %s", deviceFCMTokenToken, result.getErrorCode());
            } else {
                userNotification.setStatus(UserNotificationStatus.WAS_SEND);
                userNotificationService.save(userNotification);
            }
        }
    }
}

package com.tackpad.services;

import com.tackpad.models.UserNotification;
import de.bytefish.fcmjava.http.client.IFcmClient;
import de.bytefish.fcmjava.model.options.FcmMessageOptions;
import de.bytefish.fcmjava.requests.data.DataUnicastMessage;
import de.bytefish.fcmjava.requests.notification.NotificationPayload;
import de.bytefish.fcmjava.responses.FcmMessageResponse;
import de.bytefish.fcmjava.responses.FcmMessageResultItem;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class SendFCMRequestsService {

    private final IFcmClient fcmClient;

    private int id = 0;

    public SendFCMRequestsService(IFcmClient fcmClient) {
        this.fcmClient = fcmClient;
    }

    public void sendPushMessages(String deviceFCMTokenToken, UserNotification token) {
        FcmMessageOptions options = FcmMessageOptions.builder()
                .setTimeToLive(Duration.ofMinutes(2)).build();

        NotificationPayload payload = NotificationPayload.builder()
                .setBody("A Personal Message").setTitle("Personal Message").setTag("personal")
                .build();

        System.out.println("Sending personal message to: " + token);
        Map<String, Object> data = new HashMap<>();
        data.put("id", ++this.id);
        data.put("text", Math.random() * 1000);

        DataUnicastMessage message = new DataUnicastMessage(options, deviceFCMTokenToken, data, payload);
        FcmMessageResponse response = this.fcmClient.send(message);
        for (FcmMessageResultItem result : response.getResults()) {
            if (result.getErrorCode() != null) {
                System.out.printf("Sending to %s failed. Error Code %s\n", token,
                        result.getErrorCode());
            }
        }
    }
}

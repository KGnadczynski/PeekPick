package com.tackpad.scheduledtasks;

import com.tackpad.models.Message;
import com.tackpad.models.UserNotification;
import com.tackpad.models.enums.MessageStatus;
import com.tackpad.models.oauth2.User;
import com.tackpad.services.MessageByLocaleService;
import com.tackpad.services.MessageService;
import com.tackpad.services.UserNotificationService;
import com.tackpad.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.tackpad.models.enums.UserNotificationType.FREE_PLAN_UPDATED_INFO;

@Component
public class RemoveEndedPosts {

    private static final Logger log = LoggerFactory.getLogger(RemoveEndedPosts.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    public MessageService messageService;

    @Autowired
    UserNotificationService userNotificationService;

    @Autowired
    private MessageByLocaleService messageByLocaleService;

    //2 min
    @Scheduled(fixedRate = 120*1000)
    public void removeEndedPost() {
        log.info(" START REMOVE ENDED MESSAGES TASK");
        List<Message> messageList = messageService.findByStatusAndWhereEndDateIsAfter(MessageStatus.NEW, new Date());

        for (Message message : messageList) {
            message.setStatus(MessageStatus.ENDED);
            messageService.merge(message);
            createUserNotification(message);
        }

        messageList = messageService.findByStatusAndWhereExpirationDateIsAfter(MessageStatus.NEW, new Date());

        for (Message message : messageList) {
            message.setStatus(MessageStatus.ENDED);
            messageService.merge(message);
            createUserNotification(message);
        }
    }

    private void createUserNotification(Message message) {
        UserNotification userNotification = new UserNotification();
        userNotification.setUser(message.getUser());
        userNotification.setType(FREE_PLAN_UPDATED_INFO);
        userNotification.setTitle(messageByLocaleService.getMessage("expired_message_notification_title"));
        userNotification.setContent(messageByLocaleService.getMessage("expired_message_notification_content"));
        userNotificationService.save(userNotification);

    }
}

package com.tackpad.scheduledtasks;

import com.tackpad.models.Message;
import com.tackpad.models.enums.MessageStatus;
import com.tackpad.services.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class RemoveEndedPosts {

    private static final Logger log = LoggerFactory.getLogger(RemoveEndedPosts.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    public MessageService messageService;

    @Scheduled(fixedRate = 60*60*1000 )
    public void removeEndedPost() {
        log.info(" START REMOVE ENDED MESSAGES TASK");
        List<Message> messageList = messageService.getWhereEndDateIsAfter(new Date());
        for (Message message : messageList) {
            message.setStatus(MessageStatus.ENDED);
            messageService.merge(message);
        }
    }
}

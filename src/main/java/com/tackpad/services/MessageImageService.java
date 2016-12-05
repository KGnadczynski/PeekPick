package com.tackpad.services;


import com.tackpad.dao.MessageImageDao;
import com.tackpad.dao.TokenDao;
import com.tackpad.models.MessageImage;
import com.tackpad.models.Token;
import com.tackpad.models.enums.TokenType;
import com.tackpad.models.oauth2.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Message service.
 * @author Przemysaw Zynis
 */
@Component
public class MessageImageService extends BaseService {

    @Autowired
    public MessageImageDao messageImageDao;

    public MessageImage getByMessageId(Long messageId) {
        return messageImageDao.findByMessageId(messageId);
    }

    /** Save.*/
    public void save(MessageImage messageImage) {
        messageImageDao.save(messageImage);
    }

}

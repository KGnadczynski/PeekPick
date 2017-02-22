package com.tackpad.services;


import com.tackpad.dao.MessageLocationDao;
import com.tackpad.models.MessageLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MessageLocationService extends BaseService {

    @Autowired
    public MessageLocationDao messageLocationDao;

    public void save(MessageLocation messageLocation) {
        messageLocationDao.save(messageLocation);
    }

}

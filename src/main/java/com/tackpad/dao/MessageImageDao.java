package com.tackpad.dao;


import com.tackpad.models.MessageImage;
import com.tackpad.models.Token;

public interface MessageImageDao extends BaseDao<MessageImage> {
    MessageImage findByMessageId(Long messageId);
}

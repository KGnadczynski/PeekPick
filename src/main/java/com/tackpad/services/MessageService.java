package com.tackpad.services;


import com.tackpad.dao.MessageDao;
import com.tackpad.models.Message;
import com.tackpad.models.enums.MessageStatus;
import com.tackpad.models.enums.MessageType;
import com.tackpad.requests.enums.ListingSortType;
import com.tackpad.responses.MessagePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Message service.
 * @author Przemysaw Zynis
 */
@Component
public class MessageService extends BaseService {

    @Autowired
    public MessageDao messageDao;

    @Cacheable(value="messagePages", cacheManager="timeoutCacheManager")
    public MessagePage getPage(Integer pageNum, Integer pageSize, List<Long> messageIdList, Long companyBranchId, Long companyId,
                               List<Long> companyCategoryMainIdList,
                               List<Long> companyCategoryIdList, List<MessageType> messageTypeList,
                               List<MessageStatus> statusList,
                               Double latitude, Double longitude, Double range, String searchTerm,
                               ListingSortType listingSortType) throws ParseException {

        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }

        if (listingSortType == null) {
            listingSortType = ListingSortType.CREATE_DATE;
        }

        if (statusList == null) {
            statusList = Collections.singletonList(MessageStatus.NEW);
        }

        MessagePage response = new MessagePage();
        response.objectList = messageDao.getPage(pageNum - 1, pageSize, messageIdList, companyBranchId, companyId, companyCategoryMainIdList,
                companyCategoryIdList, messageTypeList, statusList, latitude, longitude, range, searchTerm, listingSortType);

        if (response.objectList.size() < pageSize) {
            response.isLastPage = true;
        }

        return response;
    }

    public Message getById(Long messageId) {
        return messageDao.findById(messageId);
    }

    public Long getCount(Long companyId) {
        return messageDao.findCount(companyId);
    }

    public void save(Message message) {
        messageDao.save(message);
    }

    public void delete(Message message) {
        messageDao.delete(message);
    }

    public void merge(Message message) {
        messageDao.merge(message);
    }

    public List<Message> getWhereEndDateIsAfter(Date date) {
        return messageDao.findWhereEndDateIsAfter(date);
    }
}

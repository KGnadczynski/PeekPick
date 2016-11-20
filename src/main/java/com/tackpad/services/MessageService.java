package com.tackpad.services;


import com.tackpad.dao.MessageDao;
import com.tackpad.models.Message;
import com.tackpad.models.enums.MessageType;
import com.tackpad.requests.enums.ListingSortType;
import com.tackpad.responses.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;

/**
 * Message service.
 * @author Przemysaw Zynis
 */
@Component
public class MessageService extends BaseService {

    @Autowired
    public MessageDao messageDao;

    /**
     *
     * retrieve a window of accounts
     *
     * @return List of Accounts
     */
    public Page<Message> getPage(Integer pageNum, Integer pageSize, List<Long> messageIdList, Long companyBranchId, Long companyId,
                                 List<Long> companyCategoryMainIdList,
                                 List<Long> companyCategoryIdList, List<MessageType> messageTypeList,
                                 Double latitude, Double longitude, Integer range, String searchTerm,
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

        Page<Message> response = new Page<>();
        response.objectList = messageDao.getPage(pageNum - 1, pageSize, messageIdList, companyBranchId, companyId, companyCategoryMainIdList,
                companyCategoryIdList, messageTypeList, latitude, longitude, range, searchTerm, listingSortType);

        if (response.objectList.size() < pageSize) {
            response.isLastPage = true;
        }

        return response;
    }

    /** Save.*/
    public void save(Message message) {
        messageDao.save(message);
    }

}

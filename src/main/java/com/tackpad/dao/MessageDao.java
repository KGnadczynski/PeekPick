package com.tackpad.dao;



import com.tackpad.models.Message;
import com.tackpad.models.enums.MessageStatus;
import com.tackpad.models.enums.MessageType;
import com.tackpad.requests.enums.ListingSortType;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface MessageDao extends BaseDao<Message> {
	List<Message> getPage(int page, int pageSize, List<Long> messageIdList, Long companyBranchId, Long companyId, List<Long> companyCategoryIdList,
						  List<Long> companyCategoryMainIdList, List<MessageType> messageTypeList,
						  List<MessageStatus> statusList, Double latitude,
						  Double longitude, Double range, String searchTerm, ListingSortType listingSortType) throws ParseException;

    Long findCount(Long companyId);

	List<Message> findByStatusAndWhereEndDateIsAfter(MessageStatus status, Date date);
}

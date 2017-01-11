package com.tackpad.dao;


import com.tackpad.models.Company;
import com.tackpad.models.CompanyBranch;
import com.tackpad.models.CompanyCategory;
import com.tackpad.models.Message;
import com.tackpad.models.enums.MessageStatus;
import com.tackpad.models.enums.MessageType;
import com.tackpad.requests.enums.ListingSortType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Repository
@Transactional
public class MessageDaoImpl extends BaseDaoImpl<Message> implements MessageDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<Message> getPage(int page, int pageSize, List<Long> messageIdList, Long companyBranchId,  Long companyId,
								 List<Long> companyCategoryMainIdList, List<Long> companyCategoryIdList,
								 List<MessageType> messageTypeList,
								 Double latitude, Double longitude, Integer range, String searchTerm,
								 ListingSortType listingSortType) throws ParseException {

		Session session = sessionFactory.getCurrentSession();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT m.id as messageId, " +
				"m.type as messageType, " +
				"m.startDate as messageStartDate, " +
				"m.endDate as messageEndDate, " +
				"m.createDate as messageCreateDate, " +
				"m.content as messageContent, " +
				"m.status as messageStatus, " +
				"cb.name as companyBranchName, " +
				"cb.city as companyCity, " +
				"cb.id as companyBranchId, " +
				"cb.street as companyBranchStreet, " +
				"cb.streetNo as companyBranchStreetNo, " +
				"cb.latitude as companyBranchLatitude, " +
				"cb.longitude as companyBranchLongitude, " +
				"c.id as companyId, " +
				"c.name as companyName, " +
				"cc.id as companyCategoryId, " +
				"cc.name as companyCategoryName ");

		if (latitude != null && longitude != null) {
			sql.append(", ( 6371 * acos( cos( radians(:ulatitude) ) " +
					"* cos( radians( cb.latitude ) ) " +
					"* cos( radians( cb.longitude ) - radians(:ulongitude) ) " +
					"+ sin( radians(:ulatitude) ) " +
					"* sin( radians( cb.latitude ) ) ) ) AS distance ");
		}

		sql.append("FROM message as m ");

		sql.append("LEFT JOIN companyBranch as cb ON cb.id = m.companyBranch_id ");
		sql.append("LEFT JOIN company c ON c.id = cb.company_id ");
		sql.append("LEFT JOIN companyCategory cc ON cc.id = c.category_id ");
		sql.append("LEFT JOIN companyCategory ccp ON ccp.id = cc.parentCategory_id where 1=1 ");

		if (companyBranchId != null) {
			sql.append(" and cb.id = :companyBranchId ");
		}

		if (messageIdList != null) {
			sql.append(" and m.id IN ('" + StringUtils.join(messageIdList, "','") + "') ");
		}

		if (companyId != null) {
			sql.append(" and c.id = :companyId ");
		}

		if (companyCategoryMainIdList != null) {
			sql.append(" and ccp.id IN (" + StringUtils.join(companyCategoryMainIdList, ",") + ") ");
		}

		if (companyCategoryIdList != null) {
			sql.append(" and c.category_id IN (" + StringUtils.join(companyCategoryIdList, ",") + ") ");
		}

		if (messageTypeList != null) {
			sql.append(" and m.type IN ('" + StringUtils.join(messageTypeList, "','") + "') ");
		}

		if (searchTerm != null) {
			sql.append(" and c.name LIKE :searchTerm OR cb.name LIKE :searchTerm OR m.content LIKE :searchTerm ");
		}

		if (range != null) {
			sql.append(" HAVING distance < :range ");
		}

		switch (listingSortType) {
			case DISTANCE:
				sql.append("ORDER BY distance ");
				break;
			case CREATE_DATE:
				sql.append("ORDER BY messageCreateDate desc");
				break;
		}

		SQLQuery query = session.createSQLQuery(sql.toString());

		if (companyBranchId != null) {
			query.setParameter("companyBranchId", companyBranchId);
		}

		if (companyId != null) {
			query.setParameter("companyId", companyId);
		}

		if (latitude != null && longitude != null) {
			query.setParameter("ulongitude", longitude);
			query.setParameter("ulatitude", latitude);
		}

		if (searchTerm != null) {
			query.setParameter("searchTerm", "%" + searchTerm + "%");
		}

		if (range != null) {
			query.setParameter("range", range);
		}


		query.setFirstResult(pageSize * page);
		query.setMaxResults(pageSize);

		List<Object[]> rows = query.list();
		List<Message> messageList = new ArrayList<>();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

		for(Object[] row : rows){
			Message message = new Message();
			message.id = (Long.parseLong(row[0].toString()));
			message.type = MessageType.valueOf(row[1].toString());
			message.startDate = format.parse(row[2].toString());
			message.endDate = (row[3] != null) ? format.parse(row[3].toString()) : null;
			message.createDate = format.parse(row[4].toString());
			message.content = row[5].toString();
			message.status = MessageStatus.valueOf(row[6].toString());
			messageList.add(message);

			CompanyBranch companyBranch = new CompanyBranch();
			companyBranch.name = row[7].toString();
			companyBranch.city = row[8].toString();
			companyBranch.id = (Long.parseLong(row[9].toString()));
			companyBranch.street = row[10].toString();
			companyBranch.streetNo = row[11].toString();
			companyBranch.latitude = Double.valueOf(row[12].toString());
			companyBranch.longitude = Double.valueOf(row[13].toString());

			Company company = new Company();
			company.id = (Long.parseLong(row[14].toString()));
			company.name = row[15].toString();

			companyBranch.company = company;
			message.companyBranch = companyBranch;

			CompanyCategory companyCategory = new CompanyCategory();
			companyCategory.id = Long.parseLong(row[16].toString());
			companyCategory.name = row[17].toString();

			company.category = companyCategory;

			if (latitude != null && longitude != null) {
				message.distance = Double.parseDouble(row[18].toString());
			}
		}

		return messageList;
	}

	@Override
	public Integer findCount(Long companyId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Message.class);
		criteria.add(Restrictions.eq("companyBranch.company.id", companyId));
		return (Integer) criteria.uniqueResult();
	}

	@Override
	public Message findById(Long id) {
		Session session = sessionFactory.getCurrentSession();
		return (Message) session.get(Message.class, id);
	}

}

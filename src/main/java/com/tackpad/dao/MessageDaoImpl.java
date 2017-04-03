package com.tackpad.dao;


import com.tackpad.models.*;
import com.tackpad.models.enums.MessageStatus;
import com.tackpad.models.enums.MessageType;
import com.tackpad.models.oauth2.User;
import com.tackpad.requests.enums.ListingSortType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
								 List<MessageStatus> statusList,
								 Double latitude, Double longitude, Double range, String searchTerm,
								 ListingSortType listingSortType) throws ParseException {

		Session session = sessionFactory.getCurrentSession();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT MES.id as messageId, " +
				"MES.type as messageType, " +
				"MES.startDate as messageStartDate, " +
				"MES.endDate as messageEndDate, " +
				"MES.createDate as messageCreateDate, " +
				"MES.content as messageContent, " +
				"MES.status as messageStatus, " +
				"USR.id as userId, " +
				"USR.email as userEmail, " +
				"USR.name as userName, " +
				"COM.id as companyId, " +
				"COM.name as companyName, " +
				"COM_CAT.id as companyCategoryId, " +
				"COM_CAT.name as companyCategoryName, " +
				"PAR_COM_CAT.id as mainCategoryId, " +
				"PAR_COM_CAT.name as mainCategoryName, " +
				"MES_LOC.id as messageLocationId, " +
				"MES_LOC.city as messageLocationCity, " +
				"MES_LOC.street as messageLocationStreet, " +
				"MES_LOC.streetNo as messageLocationStreetNo, " +
				"MES_LOC.latitude as messageLocationLatitude, " +
				"MES_LOC.longitude as messageLocationLongitude, " +
				"MES_LOC.address as messageLocationAddress, " +
				"MES_LOC.name as messageLocationName, " +
				"count(MESS_COM_BRA.Message_id) as companyBranchCount ");

		if (latitude != null && longitude != null) {
			
			sql.append(", (CASE WHEN location_id IS NULL " +
					"THEN " +
					"(SELECT ( 6371 * acos( cos( radians(:ulatitude) ) " +
					"* cos( radians( COM_BRA.latitude ) ) " +
					"* cos( radians( COM_BRA.longitude ) - radians(:ulongitude) ) " +
					"+ sin( radians(:ulatitude) ) " +
					"* sin( radians( COM_BRA.latitude )))) as distance from companybranch as COM_BRA " +
					"                LEFT JOIN message_companybranch as MESS_COM_BRA ON COM_BRA.id = MESS_COM_BRA.companyBranchList_id " +
					"                where MESS_COM_BRA.Message_id = MES.id " +
					"                order by distance limit 1 ) " +
					"ELSE " +
					"( 6371 * acos( cos( radians(:ulatitude) ) " +
					"* cos( radians( MES_LOC.latitude ) ) " +
					"* cos( radians( MES_LOC.longitude ) - radians(:ulongitude) ) " +
					"+ sin( radians(:ulatitude) ) " +
					"* sin( radians( MES_LOC.latitude ) ) ) ) " +
					"            END ) as distance ");
		}


		sql.append("FROM message as MES ");
		sql.append("LEFT JOIN messagelocation as MES_LOC ON MES_LOC.id = MES.location_id ");
		sql.append("LEFT JOIN user as USR ON USR.id = MES.user_id ");
		sql.append("LEFT JOIN company as COM ON COM.id = USR.company_id ");
		sql.append("LEFT JOIN companyCategory COM_CAT ON COM_CAT.id = COM.category_id ");
		sql.append("LEFT JOIN companyCategory PAR_COM_CAT ON PAR_COM_CAT.id = COM_CAT.parentCategory_id ");

		sql.append("INNER JOIN message_companybranch MESS_COM_BRA On MESS_COM_BRA.Message_id = MES.id ");
		sql.append("INNER JOIN companybranch COM_BRA On COM_BRA.id = MESS_COM_BRA.companyBranchList_id where 1=1 ");

		if (companyBranchId != null) {
			sql.append(" and COM_BRA.id = :companyBranchId ");
		}

		if (messageIdList != null) {
			sql.append(" and MES.id IN ('" + StringUtils.join(messageIdList, "','") + "') ");
		}

		if (companyId != null) {
			sql.append(" and COM.id = :companyId ");
		}

		if (companyCategoryMainIdList != null) {
			sql.append(" and PAR_COM_CAT.id IN (" + StringUtils.join(companyCategoryMainIdList, ",") + ") ");
		}

		if (companyCategoryIdList != null) {
			sql.append(" and COM.category_id IN (" + StringUtils.join(companyCategoryIdList, ",") + ") ");
		}

		if (messageTypeList != null) {
			sql.append(" and MES.type IN ('" + StringUtils.join(messageTypeList, "','") + "') ");
		}

		if (statusList != null) {
			sql.append(" and MES.status IN ('" + StringUtils.join(statusList, "','") + "') ");
		}

		if (searchTerm != null) {
			sql.append(" and COM.name LIKE :searchTerm OR COM_BRA.name LIKE :searchTerm OR MES.content LIKE :searchTerm ");
		}

		sql.append(" and MES.status NOT LIKE '%DELETE%' ");

		sql.append(" GROUP BY MESS_COM_BRA.Message_id ");

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
			case END_DATE:
				sql.append("ORDER BY messageEndDate desc");
				break;
			case START_DATE:
				sql.append("ORDER BY messageStartDate desc");
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
		DateTimeFormatter format = DateTimeFormatter.ofPattern("Hyyyy-MM-dd HH:mm:ss");

		for(Object[] row : rows){
			Message message = new Message();
			message.setId(Long.parseLong(row[0].toString()));
			message.setType(MessageType.valueOf(row[1].toString()));
			message.setStartDate(ZonedDateTime.parse(row[2].toString(), format));
			message.setEndDate((row[3] != null) ? ZonedDateTime.parse(row[3].toString(), format) : null);
			message.setCreateDate(ZonedDateTime.parse(row[4].toString(), format));
			message.setContent(row[5].toString());
			message.setStatus(MessageStatus.valueOf(row[6].toString()));
			messageList.add(message);

			User user = new User();
			user.setId(Long.parseLong(row[7].toString()));
			user.setLogin(row[8].toString());
			user.setName(row[9].toString());
			message.setUser(user);

			Company company = new Company();
			company.setId(Long.valueOf(row[10].toString()));
			company.setName(row[11].toString());
			user.setCompany(company);

			CompanyCategory companyCategory = new CompanyCategory();
			companyCategory.setId(Long.parseLong(row[12].toString()));
			companyCategory.setName(row[13].toString());
			company.setCategory(companyCategory);

			CompanyCategory parentCompanyCategory = new CompanyCategory();
			parentCompanyCategory.setId(Long.parseLong(row[14].toString()));
			parentCompanyCategory.setName(row[15].toString());
			companyCategory.setParentCategory(parentCompanyCategory);

			if (row[16] != null) {
				MessageLocation messageLocation = new MessageLocation();
				messageLocation.setId(Long.valueOf(row[16].toString()));
				messageLocation.setCity(row[17] != null ? row[17].toString() : null);
				messageLocation.setStreet(row[18] != null ? row[18].toString() : null);
				messageLocation.setStreetNo(row[19] != null ? row[19].toString() : null);
				messageLocation.setLatitude(Double.valueOf(row[20].toString()));
				messageLocation.setLongitude(Double.valueOf(row[21].toString()));
				messageLocation.setAddress(row[22] != null ? row[22].toString() : null);
				messageLocation.setName(row[23] != null ? row[23].toString() : null);
				message.setLocation(messageLocation); ;
			}

			message.setCompanyBranchCount(Integer.parseInt(row[24].toString()));

			if (latitude != null && longitude != null) {
				message.setDistance(Double.parseDouble(row[25].toString()));
			}

		}

		return messageList;
	}

	@Override
	public Long findCount(Long companyId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Message.class, "m");
		criteria.createAlias("m.user", "USR");
		criteria.createAlias("USR.company", "COM");
		criteria.add(Restrictions.eq("COM.id", companyId));
		return (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	@Override
	public List<Message> findByStatusAndWhereEndDateIsAfter(MessageStatus status, Date date) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Message.class, "m");
		criteria.add(Restrictions.lt("endDate", date));
		criteria.add(Restrictions.eq("status", status));
		return (List<Message>) criteria.list();
	}

	@Override
	public Message findById(Long id) {
		Session session = sessionFactory.getCurrentSession();
		return (Message) session.get(Message.class, id);
	}

}

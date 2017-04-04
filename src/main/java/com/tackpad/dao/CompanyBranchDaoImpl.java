package com.tackpad.dao;


import com.tackpad.models.*;
import com.tackpad.models.enums.CompanyBranchStatus;
import com.tackpad.models.enums.MessageStatus;
import com.tackpad.models.enums.MessageType;
import com.tackpad.models.oauth2.User;
import com.tackpad.requests.enums.ListingSortType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
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
public class CompanyBranchDaoImpl extends BaseDaoImpl<CompanyBranch> implements CompanyBranchDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<CompanyBranch> getPage(int page, int pageSize) {
		Session session = sessionFactory.getCurrentSession();
		final Criteria criteria = session.createCriteria(CompanyBranch.class);
		criteria.setMaxResults(pageSize);
		criteria.setFirstResult(pageSize * page);
		criteria.add(Restrictions.not(Restrictions.eq("status", CompanyBranchStatus.DELETE)));
		return criteria.list();
	}

	@Override
	public List<CompanyBranch> findListByCompanyId(Long companyId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CompanyBranch.class);
		criteria.add(Restrictions.eq("company.id", companyId));
		criteria.add(Restrictions.not(Restrictions.eq("status", CompanyBranchStatus.DELETE)));
		criteria.addOrder(Order.desc("isMain"));
		return criteria.list();
	}

	@Override
	public List<CompanyBranch> getPage(int page, int pageSize, List<Long> messageIdList, Long companyBranchId, Long companyId,
									   Double latitude, Double longitude, Double range,
									   String searchTerm, ListingSortType listingSortType) throws ParseException {

		Session session = sessionFactory.getCurrentSession();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT " +
				"COM_BRA.id as companyBranchId, " +
				"COM_BRA.name as companyBranchName, " +
				"COM_BRA.city as companyBranchCity, " +
				"COM_BRA.street as companyBranchStreet, " +
				"COM_BRA.streetNo as companyBranchStreetNo, " +
				"COM_BRA.latitude as companyBranchLatitude, " +
				"COM_BRA.longitude as companyBranchLongitude, " +
				"COM_BRA.description as companyBranchDescription, " +
				"COM_BRA.website as companyBranchWebsite, " +
				"COM_BRA.phoneNumber as companyBranchPhoneNumber, " +
				"COM_BRA.email as companyBranchEmail, " +
				"COM_BRA.openingHours as companyBranchOpeningHours, " +
				"COM_BRA.createDate as companyBranchCreateDate, " +
				"COM_BRA.isMain as companyBranchIsMain, " +
				"COM.id as companyId, " +
				"COM.name as companyName, " +
				"COM.createDate as companyCreateDate ");

		if (latitude != null && longitude != null) {

			sql.append(", ( 6371 * acos( cos( radians(:ulatitude) ) " +
					"* cos( radians( COM_BRA.latitude ) ) " +
					"* cos( radians( COM_BRA.longitude ) - radians(:ulongitude) ) " +
					"+ sin( radians(:ulatitude) ) " +
					"* sin( radians( COM_BRA.latitude ) ) ) ) " +
					"  as distance ");
		}

		sql.append("FROM companybranch as COM_BRA ");
		sql.append("LEFT JOIN company as COM ON COM.id = COM_BRA.company_id ");
		sql.append("INNER JOIN message_companybranch MESS_COM_BRA On MESS_COM_BRA.companyBranchList_id = COM_BRA.id ");

		if (messageIdList == null) {
			sql.append(" and COM_BRA.status != '" + CompanyBranchStatus.DELETE + "' ");
		}

		if (companyBranchId != null) {
			sql.append(" and COM_BRA.id = :companyBranchId ");
		}

		if (companyId != null) {
			sql.append(" and COM.id = :companyId ");
		}

		if (messageIdList != null) {
			sql.append(" and MESS_COM_BRA.Message_id IN ('" + StringUtils.join(messageIdList, "','") + "') ");
		}

		sql.append(" GROUP BY MESS_COM_BRA.companyBranchList_id ");

		if (latitude != null && longitude != null) {
			switch (listingSortType) {
				case DISTANCE:
					sql.append("ORDER BY distance ");
					break;
				case CREATE_DATE:
					sql.append("ORDER BY companyBranchCreateDate desc");
					break;
			}
		} else {
			sql.append("ORDER BY companyBranchCreateDate desc");
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

		query.setFirstResult(pageSize * page);
		query.setMaxResults(pageSize);

		List<Object[]> rows = query.list();
		List<CompanyBranch> companyBranchList = new ArrayList<>();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

		for(Object[] row : rows){
			CompanyBranch companyBranch = new CompanyBranch();
			companyBranch.setId(Long.parseLong(row[0].toString()));
			companyBranch.setName(row[1].toString());
			companyBranch.setCity(row[2].toString());
			companyBranch.setStreet(row[3].toString());
			companyBranch.setStreetNo(row[4].toString());
			companyBranch.setLatitude(Double.valueOf(row[5].toString()));
			companyBranch.setLongitude(Double.valueOf(row[6].toString()));
			companyBranch.setDescription(String.valueOf(row[7]));
			companyBranch.setWebsite(String.valueOf(row[8]));
			companyBranch.setPhoneNumber(String.valueOf(row[9]));
			companyBranch.setEmail(String.valueOf(row[10]));
			companyBranch.setOpeningHours(String.valueOf(row[11]));
			companyBranch.setCreateDate(format.parse(row[12].toString()));
			companyBranch.setMain(Boolean.parseBoolean(row[13].toString()));
			companyBranchList.add(companyBranch);

			Company company = new Company();
			company.setId(Long.parseLong(row[14].toString()));
			company.setName(String.valueOf(row[15]));
			company.setCreateDate(format.parse(String.valueOf(row[16])));
			companyBranch.setCompany(company);

			if (latitude != null && longitude != null) {
				companyBranch.setDistance(Double.parseDouble(row[17].toString()));
			}
		}

		return companyBranchList;
	}

	@Override
	public CompanyBranch getMainCompanyBranch(Long companyId) {
		Session session = sessionFactory.getCurrentSession();
		final Criteria criteria = session.createCriteria(CompanyBranch.class);
		criteria.add(Restrictions.eq("company.id", companyId));
		criteria.add(Restrictions.eq("isMain", true));
		return (CompanyBranch) criteria.uniqueResult();
	}

	@Override
	public CompanyBranch findById(Long id) {
		Session session = sessionFactory.getCurrentSession();
		return (CompanyBranch) session.get(CompanyBranch.class, id);
	}

}

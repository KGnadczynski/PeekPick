package com.tackpad.dao;


import com.tackpad.models.MessageLocation;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public class MessageLocationDaoImp extends BaseDaoImpl<MessageLocation> implements MessageLocationDao {

}

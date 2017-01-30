package com.tackpad.dao;


import com.tackpad.models.Image;

public interface MessageImageDao extends BaseDao<Image> {
    Image findByMessageId(Long messageId);
    Image findByCompanyId(Long companyId);
}

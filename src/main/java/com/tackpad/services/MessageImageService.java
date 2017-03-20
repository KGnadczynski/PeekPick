package com.tackpad.services;


import com.tackpad.dao.MessageImageDao;
import com.tackpad.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Message service.
 * @author Przemysaw Zynis
 */
@Component
public class MessageImageService extends BaseService {

    @Autowired
    public MessageImageDao messageImageDao;

    @Cacheable(value = "images", cacheManager="timeoutCacheManager")
    public Image getByMessageId(Long messageId) {
        return messageImageDao.findByMessageId(messageId);
    }

    @Cacheable(value = "images", cacheManager="timeoutCacheManager")
    public Image getByCompanyId(Long companyId) {
        return messageImageDao.findByCompanyId(companyId);
    }

    /** Save.*/
    public void save(Image image) {
        messageImageDao.save(image);
    }

    public void delete(Image image) {
        messageImageDao.delete(image);
    }
}

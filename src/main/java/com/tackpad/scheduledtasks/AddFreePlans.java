package com.tackpad.scheduledtasks;

import com.tackpad.models.CompanyAvailableMessageCount;
import com.tackpad.models.Message;
import com.tackpad.models.UserNotification;
import com.tackpad.models.enums.MessageStatus;
import com.tackpad.models.oauth2.User;
import com.tackpad.services.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceSupport;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.tackpad.models.enums.UserNotificationType.FREE_PLAN_UPDATED_INFO;

@Component
public class AddFreePlans {

    private static final Logger log = LoggerFactory.getLogger(AddFreePlans.class);

    private static final int FREE_PLAN_MESSAGE_COUNT = 1;

    @Autowired
    public CompanyAvailableMessageCountService companyAvailableMessageCountService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserNotificationService userNotificationService;

    @Autowired
    private MessageByLocaleService messageByLocaleService;

    @Scheduled(fixedRate = 10000)
    public void removeEndedPost() {
        log.info(" ADD FREE PLANS TASK");
        List<CompanyAvailableMessageCount> companyAvailableMessageCountList
                = companyAvailableMessageCountService.getListByAvailableMessageCountAndLastUpdateForFreePlanDateAfter(0, DateTime.now().minusWeeks(2));

        for (CompanyAvailableMessageCount companyAvailableMessageCount : companyAvailableMessageCountList) {
            log.info(" ADD FREE PLAN FOR COMPANY {COMPANY ID}:" + companyAvailableMessageCount.getCompany());

            companyAvailableMessageCount.setAvailableMessageCount(FREE_PLAN_MESSAGE_COUNT);
            companyAvailableMessageCount.setLastUpdateForFreePlanDate(DateTime.now().toDate());
            companyAvailableMessageCountService.merge(companyAvailableMessageCount);

            createUserNotification(companyAvailableMessageCount.getCompany().getId());
        }
    }

    private void createUserNotification(Long companyId) {
        List<User> userList = userService.getByCompanyId(companyId);
        for (User user : userList) {
            UserNotification userNotification = new UserNotification();
            userNotification.setUser(user);
            userNotification.setType(FREE_PLAN_UPDATED_INFO);
            userNotification.setTitle(messageByLocaleService.getMessage("add_free_plan_notification_title"));
            userNotification.setContent(messageByLocaleService.getMessage("add_free_plan_notification_content"));
            userNotificationService.save(userNotification);
        }

    }
}

package com.tackpad.scheduledtasks;

import com.tackpad.models.CompanyCredit;
import com.tackpad.models.UserNotification;
import com.tackpad.models.oauth2.User;
import com.tackpad.services.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.tackpad.models.enums.UserNotificationType.FREE_PLAN_UPDATED_INFO;

@Component
public class AddFreePlans {

    private static final Logger log = LoggerFactory.getLogger(AddFreePlans.class);

    private static final int FREE_PLAN_MESSAGE_COUNT = 1;

    @Autowired
    public CompanyCreditService companyCreditService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserNotificationService userNotificationService;

    @Autowired
    private MessageByLocaleService messageByLocaleService;

    @Scheduled(fixedRate = 10000)
    public void removeEndedPost() {
        log.info(" ADD FREE PLANS TASK");
        List<CompanyCredit> companyCreditList
                = companyCreditService.getListByAvailableMessageCountAndLastUpdateForFreePlanDateAfter(0, DateTime.now().minusWeeks(2));

        for (CompanyCredit companyCredit : companyCreditList) {
            log.info(" ADD FREE PLAN FOR COMPANY {COMPANY ID}:" + companyCredit.getCompany());

            companyCredit.setCredit(FREE_PLAN_MESSAGE_COUNT);
            companyCredit.setLastUpdateForFreePlanDate(DateTime.now().toDate());
            companyCreditService.merge(companyCredit);

            createUserNotification(companyCredit.getCompany().getId());
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

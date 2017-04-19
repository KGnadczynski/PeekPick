package com.tackpad.models;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.tackpad.models.enums.MessageStatus;
import com.tackpad.models.enums.MessageType;
import com.tackpad.models.enums.UserNotificationStatus;
import com.tackpad.models.enums.UserNotificationType;
import com.tackpad.models.oauth2.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class UserNotification {

    @Id
    @GeneratedValue
    @NotNull(groups = {UpdateUserNotificationValidation.class})
    private Long id;

    /** Nazwa.*/
    @Column(nullable = false, length = 100000)
    @NotNull(groups = {CreateUserNotificationValidation.class, UpdateUserNotificationValidation.class})
    private String title;

    /** Nazwa.*/
    @Column(nullable = false, length = 100000)
    @NotNull(groups = {CreateUserNotificationValidation.class, UpdateUserNotificationValidation.class})
    private String content;

    /** Typ.*/
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = {CreateUserNotificationValidation.class, UpdateUserNotificationValidation.class})
    private UserNotificationType type;

    /** Data dodania.*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    /** Status.*/
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = {CreateUserNotificationValidation.class, UpdateUserNotificationValidation.class})
    private UserNotificationStatus status = UserNotificationStatus.NOT_SEND;

    @ManyToOne(optional = false)
    @Valid
    private User user;

    public interface CreateUserNotificationValidation {}
    public interface UpdateUserNotificationValidation {}

}

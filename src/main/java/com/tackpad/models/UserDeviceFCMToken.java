package com.tackpad.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tackpad.models.enums.DeviceType;
import com.tackpad.models.enums.ImageType;
import com.tackpad.models.enums.TokenType;
import com.tackpad.models.oauth2.User;
import com.tackpad.requests.CreateBossinessUserForm;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @see https://firebase.google.com/docs/cloud-messaging/send-message
 */
@Entity
@Getter
@Setter
public class UserDeviceFCMToken {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @Valid
    private User user;

    @Column(nullable = false)
    @NotNull(groups = UserDeviceFCMTokenValidation.class)
    private String token;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = UserDeviceFCMTokenValidation.class)
    private DeviceType deviceType;

    public interface UserDeviceFCMTokenValidation{};

}

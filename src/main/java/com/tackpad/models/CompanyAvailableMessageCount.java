package com.tackpad.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tackpad.models.enums.MessageStatus;
import com.tackpad.models.enums.MessageType;
import com.tackpad.models.oauth2.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity(name = "Company_available_message_count")
@Getter
@Setter
public class CompanyAvailableMessageCount {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(optional = false)
    @Valid
    private Company company;

    @Column(nullable = false)
    @NotNull
    private int availableMessageCount = 15;

    @Column(nullable = false)
    @NotNull
    private Date lastUpdateForFreePlanDate = new Date();

}

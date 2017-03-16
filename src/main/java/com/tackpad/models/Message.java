package com.tackpad.models;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.tackpad.models.enums.MessageStatus;
import com.tackpad.models.enums.MessageType;
import com.tackpad.models.oauth2.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Table
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    /** Nazwa.*/
    @Column(nullable = false, length = 100000)
    @NotNull(groups = CreateMessageValidation.class)
    private String content;

    /** Typ.*/
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = CreateMessageValidation.class)
    private MessageType type;

    /** Start promocji.*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "DEFAULT_TIMEZONE")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    @NotNull(groups = CreateMessageValidation.class)
    private Date startDate;

    /** Koniec promocji.*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "DEFAULT_TIMEZONE")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column
    private Date endDate;

    /** Data dodania.*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "DEFAULT_TIMEZONE")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private Date createDate = new Date();

    /** Status.*/
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = CreateMessageValidation.class)
    private MessageStatus status = MessageStatus.NEW;

    @ManyToMany
    @Valid
    @NotNull(groups = CreateMessageValidation.class)
    private List<CompanyBranch> companyBranchList;

    @ManyToOne(optional = false)
    private User user;

    @OneToOne
    private MessageLocation location;

    @Transient
    private double distance;

    @Transient
    private String mainImageUrl;

    @Transient
    private int companyBranchCount;

    @Transient
    private CompanyBranch nearestCompanyBranch;

    /** Do walidacji formularza tworzenia widomosci.*/
    public interface CreateMessageValidation {}

}

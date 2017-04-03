package com.tackpad.models;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tackpad.models.enums.MessageStatus;
import com.tackpad.models.enums.MessageType;
import com.tackpad.models.oauth2.User;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue
    @NotNull(groups = UpdateMessageValidation.class)
    private Long id;

    /** Nazwa.*/
    @Column(nullable = false, length = 100000)
    @NotNull(groups = {CreateMessageValidation.class, UpdateMessageValidation.class})
    private String content;

    /** Typ.*/
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = {CreateMessageValidation.class, UpdateMessageValidation.class})
    private MessageType type;

    /** Start promocji.*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    @Column(nullable = false)
    @NotNull(groups = {CreateMessageValidation.class, UpdateMessageValidation.class})
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    /** Koniec promocji.*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    /** Data dodania.*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    /** Status.*/
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = {CreateMessageValidation.class, UpdateMessageValidation.class})
    private MessageStatus status = MessageStatus.NEW;

    @ManyToMany
    @Valid
    @NotNull(groups = {CreateMessageValidation.class, UpdateMessageValidation.class})
    private List<CompanyBranch> companyBranchList;

    @ManyToOne(optional = false)
    @Valid
    private User user;

    @OneToOne(fetch = FetchType.EAGER , cascade=CascadeType.ALL, orphanRemoval = true)
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

    public interface UpdateMessageValidation {}

}

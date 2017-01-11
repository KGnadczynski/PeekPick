package com.tackpad.models;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.tackpad.models.enums.MessageStatus;
import com.tackpad.models.enums.MessageType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Widomość.
 * @author Przemysław Zynis
 */
@Entity
@Table
public class Message {

    @Id
    @GeneratedValue
    public Long id;

    /** Nazwa.*/
    @Column(nullable = false, length = 100000)
    @NotNull(groups = CreateMessageValidation.class)
    public String content;

    /** Typ.*/
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = CreateMessageValidation.class)
    public MessageType type;

    /** Start promocji.*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "DEFAULT_TIMEZONE")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    @NotNull(groups = CreateMessageValidation.class)
    public Date startDate;

    /** Koniec promocji.*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "DEFAULT_TIMEZONE")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column
    public Date endDate;

    /** Data dodania.*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "DEFAULT_TIMEZONE")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    public Date createDate = new Date();

    /** Status.*/
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(groups = CreateMessageValidation.class)
    public MessageStatus status = MessageStatus.NEW;

    /** Oddzial firmy.*/
    @ManyToOne(optional = false)
    public CompanyBranch companyBranch;

    @Transient
    public double distance;

    @Transient
    public String mainImageUrl;

    /** Do walidacji formularza tworzenia widomosci.*/
    public interface CreateMessageValidation {}

}

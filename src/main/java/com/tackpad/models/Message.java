package com.tackpad.models;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.tackpad.models.enums.MessageStatus;
import com.tackpad.models.enums.MessageType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Widomość.
 * @author Przemysław Zynis
 */
@Entity(name = "message")
@Table(name = "message")
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    @NotNull(groups = CreateMessageValidation.class)
    public Date startDate;

    /** Koniec promocji.*/
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    @NotNull(groups = CreateMessageValidation.class)
    public Date endDate;

    /** Data dodania.*/
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
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

    /** Do walidacji formularza tworzenia widomosci.*/
    public interface CreateMessageValidation {}

}

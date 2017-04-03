package com.tackpad.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tackpad.models.oauth2.User;
import com.tackpad.requests.CreateBossinessUserForm;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Firma.
 * @author Przemysław Zynis
 */
@Entity
@Getter
@Setter
public class Company {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NotNull(groups = {CreateComapanyValidation.class, CreateBossinessUserForm.CreateBusinessUserValidation.class, UpdateComapanyValidation.class})
    private String name;

    @Valid
    @NotNull(groups = {CreateComapanyValidation.class, CreateBossinessUserForm.CreateBusinessUserValidation.class, UpdateComapanyValidation.class})
    @ManyToOne(cascade = CascadeType.PERSIST)
    private CompanyCategory category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @Transient
    private String mainImageUrl;

    public interface CreateComapanyValidation {}
    public interface UpdateComapanyValidation {}

}

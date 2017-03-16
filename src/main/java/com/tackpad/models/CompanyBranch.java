package com.tackpad.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tackpad.requests.CreateBossinessUserForm;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Oddzial firmy
 */
@Entity
@Getter
@Setter
public class CompanyBranch {

    @Id
    @GeneratedValue
    @NotNull(groups = {UpdateCompanyBranchValidation.class})
    private Long id;

    /** Nazwa.*/
    @Column(nullable = false)
    @NotNull(groups = {CreateBossinessUserForm.CreateBusinessUserValidation.class, UpdateCompanyBranchValidation.class})
    private String name;

    /** Miasto.*/
    @Column(nullable = false)
     @NotNull(groups = {CreateBossinessUserForm.CreateBusinessUserValidation.class, UpdateCompanyBranchValidation.class})
    private String city;

    /** Ulica.*/
    @Column(nullable = false)
     @NotNull(groups = {CreateBossinessUserForm.CreateBusinessUserValidation.class, UpdateCompanyBranchValidation.class})
    private String street;

    /** Numer lokalu.*/
    @Column(nullable = false)
     @NotNull(groups = {CreateBossinessUserForm.CreateBusinessUserValidation.class, UpdateCompanyBranchValidation.class})
    private String streetNo;

    /** Długość geograficzna.*/
    @Column
     @NotNull(groups = {CreateBossinessUserForm.CreateBusinessUserValidation.class, UpdateCompanyBranchValidation.class})
    private Double latitude;

    /** Szerokość geograficzna.*/
    @Column
     @NotNull(groups = {CreateBossinessUserForm.CreateBusinessUserValidation.class, UpdateCompanyBranchValidation.class})
    private Double longitude;

    @Column(length = 1000)
    @Length(max = 1000)
    private String description;

    @Column(length = 100)
    @Length(max = 100)
    private String website;

    @Column(length = 20)
    @Length(max = 20)
    private String phoneNumber;

    @Column(length = 30)
    @Length(max = 30)
    private String email;

    @Column(length = 200)
    @Length(max = 200)
    private String openingHours;

    @Valid
     @NotNull(groups = {CreateBossinessUserForm.CreateBusinessUserValidation.class, UpdateCompanyBranchValidation.class})
    @ManyToOne
    private Company company;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "DEFAULT_TIMEZONE")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private Date createDate = new Date();

    @Column(nullable = false)
    private boolean isMain;

    @Transient
    private double distance;

    public interface UpdateCompanyBranchValidation {}
}

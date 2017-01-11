package com.tackpad.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tackpad.models.oauth2.User;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Firma.
 * @author Przemysław Zynis
 */
@Entity
@Table
public class Company {

    @Id
    @GeneratedValue
    public Long id;

    /** Nazwa.*/
    @Column(nullable = false)
    @NotNull(groups = {CreateComapanyValidation.class, User.CreateBusinessUserValidation.class})
    public String name;

    /** Miasto.*/
    @Column(nullable = false)
    @NotNull(groups = {CreateComapanyValidation.class, User.CreateBusinessUserValidation.class})
    public String city;

    /** Ulica.*/
    @Column(nullable = false)
    @NotNull(groups = {CreateComapanyValidation.class, User.CreateBusinessUserValidation.class})
    public String street;

    /** Numer lokalu.*/
    @Column(nullable = false)
    @NotNull(groups = {CreateComapanyValidation.class, User.CreateBusinessUserValidation.class})
    public String streetNo;

    /** Długość geograficzna.*/
    @Column(nullable = false)
    @NotNull(groups = {CreateComapanyValidation.class, User.CreateBusinessUserValidation.class})
    public Double latitude;

    /** Szerokość geograficzna.*/
    @Column(nullable = false)
    @NotNull(groups = {CreateComapanyValidation.class, User.CreateBusinessUserValidation.class})
    public Double longitude;


    /** Kategoria.*/
    @Valid
    @NotNull(groups = {CreateComapanyValidation.class, User.CreateBusinessUserValidation.class})
    @ManyToOne(cascade = CascadeType.PERSIST)
    public CompanyCategory category;

    /** Data dodania.*/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "DEFAULT_TIMEZONE")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    public Date createDate = new Date();

    @Column(length = 1000)
    @Length(max = 1000)
    public String description;

    @Column(length = 100)
    @Length(max = 100)
    public String website;

    @Column(length = 20)
    @Length(max = 20)
    public String phoneNumber;

    @Column(length = 200)
    @Length(max = 200)
    public String openingHours;

    /** Do walidacji formularza tworzenia firmy.*/
    public interface CreateComapanyValidation {}

}

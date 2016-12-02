package com.tackpad.models;


import com.tackpad.requests.CreateBusinessUserForm;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Firma.
 * @author Przemysław Zynis
 */
@Entity(name = "company")
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue
    public Long id;

    /** Nazwa.*/
    @Column(nullable = false)
    @NotNull(groups = {CreateComapanyValidation.class, CreateBusinessUserForm.CreateBossinessValidation.class})
    public String name;

    /** Miasto.*/
    @Column(nullable = false)
    @NotNull(groups = {CreateComapanyValidation.class, CreateBusinessUserForm.CreateBossinessValidation.class})
    public String city;

    /** Ulica.*/
    @Column(nullable = false)
    @NotNull(groups = {CreateComapanyValidation.class, CreateBusinessUserForm.CreateBossinessValidation.class})
    public String street;

    /** Numer lokalu.*/
    @Column(nullable = false)
    @NotNull(groups = {CreateComapanyValidation.class, CreateBusinessUserForm.CreateBossinessValidation.class})
    public String streetNo;

    /** Długość geograficzna.*/
    @Column(nullable = false)
    public Double latitude;

    /** Szerokość geograficzna.*/
    @Column(nullable = false)
    public Double longitude;

    /** Kategoria.*/
    @Valid
    @NotNull(groups = {CreateComapanyValidation.class, CreateBusinessUserForm.CreateBossinessValidation.class})
    @ManyToOne(cascade = CascadeType.PERSIST)
    public CompanyCategory category;



    /** Do walidacji formularza tworzenia firmy.*/
    public interface CreateComapanyValidation {}

}

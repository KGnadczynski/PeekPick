package com.tackpad.models;


import com.tackpad.models.oauth2.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Firma.
 * @author Przemysław Zynis
 */
@Entity
@Table
public class CompanyCategory {

    @Id
    @GeneratedValue
    @NotNull(groups = {Company.CreateComapanyValidation.class, User.CreateBusinessUserValidation.class, Company.UpdateComapanyValidation.class})
    public Long id;

    /** Nazwa.*/
    @Column(nullable = false)
    public String name;

    /** Kategoria nedrzędna.*/
    @ManyToOne
    public CompanyCategory parentCategory;

}

package com.tackpad.models;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Firma.
 * @author Przemysław Zynis
 */
@Entity(name = "companyCategory")
@Table(name = "companyCategory")
public class CompanyCategory {

    @Id
    @GeneratedValue
    @NotNull(groups = Company.CreateComapanyValidation.class)
    public Long id;

    /** Nazwa.*/
    @Column(nullable = false)
    public String name;

    /** Kategoria nedrzędna.*/
    @ManyToOne
    public CompanyCategory parentCategory;

}

package com.tackpad.models;


import com.tackpad.models.oauth2.User;
import com.tackpad.requests.CreateBossinessUserForm;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Firma.
 * @author Przemysław Zynis
 */
@Entity(name = "Company_category")
@Getter
@Setter
public class CompanyCategory {

    @Id
    @GeneratedValue
    @NotNull(groups = {Company.CreateComapanyValidation.class, CreateBossinessUserForm.CreateBusinessUserValidation.class, Company.UpdateComapanyValidation.class})
    private Long id;

    /** Nazwa.*/
    @Column(nullable = false)
    private String name;

    /** Kategoria nedrzędna.*/
    @ManyToOne
    private CompanyCategory parentCategory;

}

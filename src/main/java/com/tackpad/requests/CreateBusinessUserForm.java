package com.tackpad.requests;

import com.tackpad.models.Company;
import com.tackpad.models.oauth2.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by Przemysław Żynis on 02.12.2016.
 */
public class CreateBusinessUserForm {

    @Valid
    @NotNull(groups = CreateBossinessValidation.class)
    public User user;

    @Valid
    @NotNull(groups = CreateBossinessValidation.class)
    public Company company;

    /** Do walidacji formularza tworzenia widomosci.*/
    public interface CreateBossinessValidation {}
}

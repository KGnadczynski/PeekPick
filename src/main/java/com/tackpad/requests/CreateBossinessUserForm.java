package com.tackpad.requests;

import com.tackpad.models.CompanyBranch;
import com.tackpad.models.Token;
import com.tackpad.models.oauth2.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateBossinessUserForm {

    @Valid
    @NotNull(groups = CreateBossinessUserForm.CreateBusinessUserValidation.class)
    private User user;

    @Valid
    @NotNull(groups = CreateBossinessUserForm.CreateBusinessUserValidation.class)
    private CompanyBranch companyBranch;

    @Valid
    @NotNull(groups = CreateBossinessUserForm.CreateBusinessUserValidation.class)
    private Token token;

    public interface CreateBusinessUserValidation{}
}

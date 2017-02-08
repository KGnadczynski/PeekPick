package com.tackpad.requests;

import com.tackpad.models.oauth2.User;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class UpdatePasswordForm {

    @NotNull
    @Length(min = 6)
    public String password;

    @NotNull
    @Length(min = 6)
    public String newPassword;
}

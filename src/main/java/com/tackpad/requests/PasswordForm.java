package com.tackpad.requests;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class PasswordForm {

    @NotNull
    @Length(min = 6)
    public String password;

}

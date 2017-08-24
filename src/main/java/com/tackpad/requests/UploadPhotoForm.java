package com.tackpad.requests;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class UploadPhotoForm {

    @NotNull
    @Length(min = 6)
    public String base64;

}

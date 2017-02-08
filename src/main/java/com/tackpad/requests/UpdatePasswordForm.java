package com.tackpad.requests;

import com.cloudinary.Singleton;
import com.cloudinary.StoredFile;
import com.cloudinary.Transformation;
import com.tackpad.models.Company;
import com.tackpad.models.oauth2.User;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class UpdatePasswordForm extends StoredFile {

    @NotNull
    public String password;

    @NotNull
    public String newPassword;
}

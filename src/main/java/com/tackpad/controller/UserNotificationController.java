package com.tackpad.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tackpad.models.*;
import com.tackpad.models.enums.TokenType;
import com.tackpad.models.enums.UserRoleType;
import com.tackpad.models.enums.UserStatus;
import com.tackpad.models.oauth2.User;
import com.tackpad.models.oauth2.UserRole;
import com.tackpad.requests.CreateBossinessUserForm;
import com.tackpad.requests.Diggits;
import com.tackpad.requests.UpdateEmailForm;
import com.tackpad.requests.UpdatePasswordForm;
import com.tackpad.responses.CompanyPage;
import com.tackpad.responses.DiggitsResponse;
import com.tackpad.responses.USerPage;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.services.*;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.ozimov.springboot.templating.mail.service.exception.CannotSendEmailException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/usernotifications")
public class UserNotificationController extends BaseController {

    @Autowired
    public UserNotificationService userNotificationService;

    @Autowired
    public UserService userService;

    @PostMapping()
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = User.class))
    ResponseEntity create(@Validated(UserNotification.CreateUserNotificationValidation.class)
                          @RequestBody UserNotification userNotification, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        if (userService.findById(userNotification.getUser().getId()) == null) {
            return badRequest(BadRequestResponseType.INVALID_USER_ID);
        }

        userNotificationService.save(userNotification);

        return success(userNotification);
    }


}

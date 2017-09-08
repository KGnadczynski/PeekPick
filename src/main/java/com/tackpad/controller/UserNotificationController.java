package com.tackpad.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tackpad.models.*;
import com.tackpad.models.enums.TokenType;
import com.tackpad.models.enums.UserNotificationStatus;
import com.tackpad.models.enums.UserRoleType;
import com.tackpad.models.enums.UserStatus;
import com.tackpad.models.oauth2.User;
import com.tackpad.models.oauth2.UserRole;
import com.tackpad.requests.CreateBossinessUserForm;
import com.tackpad.requests.Diggits;
import com.tackpad.requests.UpdateEmailForm;
import com.tackpad.requests.UpdatePasswordForm;
import com.tackpad.responses.*;
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

    @GetMapping(value = "/page/{page}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = CompanyPage.class))
    ResponseEntity getPage(Authentication authentication,
                           @PathVariable("page") int page,
                           @RequestParam(value = "pageSize", required=false) Integer pageSize,
                           @RequestParam(value = "searchTerm", required=false) String searchTerm,
                           @RequestParam(value = "userId", required=false) Long userId) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.getByEmail(userDetails.getUsername());

        if (!hasRole(UserRoleType.ROLE_ADMIN) && !currentUser.getId().equals(userId)) {
            return forbidden(BadRequestResponseType.INVALID_USER_ID);
        }

        UserNotificationPage uSerPage = userNotificationService.getPage(page, pageSize, searchTerm, userId);
        return success(uSerPage);
    }

    @GetMapping(value = "/status/{userNotificationStatus}/count")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = CompanyPage.class))
    ResponseEntity getCount(Authentication authentication, @PathVariable("userNotificationStatus") String userNotificationStatus) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.getByEmail(userDetails.getUsername());


        CountResponse countResponse = new CountResponse();
        countResponse.count = userNotificationService.gatCountByStatus(UserNotificationStatus.valueOf(userNotificationStatus), currentUser.getId());

        return success(countResponse);
    }



    @DeleteMapping(value = "/{userNotificationId}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = CompanyPage.class))
    ResponseEntity delete(Authentication authentication,
                          @PathVariable("userNotificationId") Long userNotificationId) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.getByEmail(userDetails.getUsername());
        UserNotification userNotification = userNotificationService.get(userNotificationId);

        if (!hasRole(UserRoleType.ROLE_ADMIN) && !currentUser.getId().equals(userNotification.getUser().getId())) {
            return forbidden(BadRequestResponseType.INVALID_USER_ID);
        }

        userNotification.setStatus(UserNotificationStatus.DELETED);
        userNotificationService.save(userNotification);

        return success(userNotification);
    }

    @PutMapping()
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = User.class))
    ResponseEntity update(Authentication authentication,
                          @Validated(UserNotification.UpdateUserNotificationValidation.class)
                          @RequestBody UserNotification userNotification, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.getByEmail(userDetails.getUsername());

        if (!currentUser.getId().equals(userNotification.getUser().getId())) {
            return forbidden(BadRequestResponseType.INVALID_USER_ID);
        }

        userNotificationService.save(userNotification);

        return success(userNotification);
    }
}

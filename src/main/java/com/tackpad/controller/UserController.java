package com.tackpad.controller;

import com.tackpad.models.Company;
import com.tackpad.models.CompanyBranch;
import com.tackpad.models.CompanyCategory;
import com.tackpad.models.enums.UserStatus;
import com.tackpad.models.oauth2.User;
import com.tackpad.requests.UpdateEmailForm;
import com.tackpad.requests.UpdatePasswordForm;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.services.*;
import it.ozimov.springboot.templating.mail.service.exception.CannotSendEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.io.UnsupportedEncodingException;

/**
 * Created by Przemysław Żynis on 02.12.2016.
 */
@Controller
@RequestMapping("/users")
public class UserController  extends BaseController {

    @Autowired
    public UserService userService;

    @Autowired
    public CompanyService companyService;

    @Autowired
    public CompanyBranchService companyBranchService;

    @Autowired
    public CompanyCategoryService companyCategoryService;

    @Autowired
    public SendEmailService sendEmailService;

    @Autowired
    public TokenService tokenService;

    @PostMapping("/business")
    ResponseEntity create(@Validated(User.CreateBusinessUserValidation.class)
                          @RequestBody User user, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        Company company = user.getCompany();

        if (userService.getByEmail(user.getEmail()) != null) {
            return badRequest(BadRequestResponseType.EMAIL_ADDRESS_IS_USED);
        }

        CompanyCategory companyCategory = companyCategoryService.getById(company.category.id);
        if (companyCategory == null) {
            return badRequest(BadRequestResponseType.INVALID_CATEGORY_ID);
        }
        company.category = companyCategory;

        user.setStatus(UserStatus.NON_ACTIVE);

        CompanyBranch companyBranch = new CompanyBranch();
        companyBranch.street = company.street;
        companyBranch.city = company.city;
        companyBranch.name = company.name;
        companyBranch.streetNo = company.streetNo;
        companyBranch.latitude = company.latitude;
        companyBranch.longitude = company.longitude;

        companyService.save(company);

        user.setCompany(company);

        userService.create(user);

        companyBranch.company = company;

        companyBranchService.save(companyBranch);

        try {
            String tokenValue = tokenService.createConfirmAccountToken(user);
            sendEmailService.sendRegisterEmailConfirm(user.getEmail(), company.name, tokenValue);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (CannotSendEmailException e) {
            e.printStackTrace();
        }

        return success(user);
    }

    @GetMapping("/email")
    ResponseEntity checkEmailIsUsed(@QueryParam("email") String email) {

        //Unikalnosc meila
        if (userService.getByEmail(email) != null) {
            return badRequest(BadRequestResponseType.EMAIL_ADDRESS_IS_USED);
        }

        return success();
    }

    @GetMapping("/business/me")
    ResponseEntity getMy(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());

        return success(user);
    }

    @PutMapping(value = "/password")
    ResponseEntity updatePassword(Authentication authentication,
                                  @Validated @RequestBody UpdatePasswordForm updatePasswordForm, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(updatePasswordForm.password, user.getPassword())) {
            return badRequest(BadRequestResponseType.WRONG_PASSWORD);
        }

        userService.updatePassword(user, updatePasswordForm.newPassword);

        return success(user);
    }

    @PutMapping(value = "/email")
    ResponseEntity updateEmail(Authentication authentication,
                                  @Validated @RequestBody UpdateEmailForm updateEmailForm, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());

        try {
            String tokenValue = tokenService.createChangeEmailToken(user, updateEmailForm.email);
            sendEmailService.sendChangeEmailConfirm(updateEmailForm.email, user.getCompany().name, tokenValue);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (CannotSendEmailException e) {
            e.printStackTrace();
        }

        return success(user);
    }

    @InitBinder("createBusinessUserForm")
    void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }
}

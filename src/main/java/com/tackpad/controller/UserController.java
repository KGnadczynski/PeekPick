package com.tackpad.controller;

import com.tackpad.models.Company;
import com.tackpad.models.CompanyBranch;
import com.tackpad.models.CompanyCategory;
import com.tackpad.models.Token;
import com.tackpad.models.enums.TokenType;
import com.tackpad.models.enums.UserStatus;
import com.tackpad.models.oauth2.User;
import com.tackpad.requests.CreateBossinessUserForm;
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
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.io.UnsupportedEncodingException;

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
    ResponseEntity create(@Validated(CreateBossinessUserForm.CreateBusinessUserValidation.class)
                          @RequestBody CreateBossinessUserForm createBossinessUserForm, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        User user = createBossinessUserForm.getUser();
        CompanyBranch companyBranch = createBossinessUserForm.getCompanyBranch();
        Token token = createBossinessUserForm.getToken();
        Company company = companyBranch.getCompany();

        if (userService.getByEmail(user.getEmail()) != null) {
            return badRequest(BadRequestResponseType.EMAIL_ADDRESS_IS_USED);
        }

        if (userService.getByPhoneNumber(user.getPhoneNumber()) != null) {
            return badRequest(BadRequestResponseType.PHONE_NUMBER_IS_USED);
        }

        CompanyCategory companyCategory = companyCategoryService.getById(company.getCategory().getId());
        if (companyCategory == null) {
            return badRequest(BadRequestResponseType.INVALID_CATEGORY_ID);
        }
        company.setCategory(companyCategory);

        user.setStatus(UserStatus.ACTIVE);

        companyService.save(company);

        user.setCompany(company);;

        userService.create(user);

        companyBranch.setCompany(company);
        companyBranch.setMain(true);
        companyBranchService.save(companyBranch);

        token.setTokenType(TokenType.TWITTER_AUTH);
        tokenService.save(token);

        /*try {
            String tokenValue = tokenService.createConfirmAccountToken(user);
            sendEmailService.sendRegisterEmailConfirm(user.getEmail(), company.getName(), tokenValue);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (CannotSendEmailException e) {
            e.printStackTrace();
        }*/

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


    @PostMapping("/diggits")
    ResponseEntity postDigits(@QueryParam("url") String url, @QueryParam("credentials") String credentials) {
       // UserDetails userDetails = (UserDetails) authentication.getPrincipal();
       // User user = userService.getByEmail(userDetails.getUsername());
        logger.info("Diggits url: {}: " +url);
        logger.info("Diggits credentials: {}: " +credentials);
        return success();
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

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(updateEmailForm.password, user.getPassword())) {
            return badRequest(BadRequestResponseType.WRONG_PASSWORD);
        }

        if (userService.getByEmail(updateEmailForm.email) != null) {
            return badRequest(BadRequestResponseType.EMAIL_ADDRESS_IS_USED);
        }

        try {
            String tokenValue = tokenService.createChangeEmailToken(user, updateEmailForm.email);
            sendEmailService.sendChangeEmailConfirm(updateEmailForm.email, user.getCompany().getName(), tokenValue);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (CannotSendEmailException e) {
            e.printStackTrace();
        }

        return success(user);
    }

    /*@InitBinder("createBusinessUserForm")
    void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }*/

}

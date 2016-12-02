package com.tackpad.controller;

import com.tackpad.models.Company;
import com.tackpad.models.CompanyBranch;
import com.tackpad.models.CompanyCategory;
import com.tackpad.models.Token;
import com.tackpad.models.enums.UserStatus;
import com.tackpad.models.oauth2.User;
import com.tackpad.requests.CreateBusinessUserForm;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.services.*;
import it.ozimov.springboot.templating.mail.service.exception.CannotSendEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity create(@Validated(CreateBusinessUserForm.CreateBossinessValidation.class)
                          @RequestBody CreateBusinessUserForm createBusinessUserForm, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        User user = createBusinessUserForm.user;
        Company company = createBusinessUserForm.company;

        //Unikalnosc meila
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

        //TODO pobieranie lokalizacji z googla
        companyBranch.latitude = 1.0;
        companyBranch.longitude = 1.0;
        company.latitude = 1.0;
        company.longitude = 1.0;

        userService.save(user);
        companyService.save(company);

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

        return success(createBusinessUserForm);
    }

    @GetMapping("/email/{email}")
    ResponseEntity checkEmailIsUsed(@PathVariable("email") String email) {

        //Unikalnosc meila
        if (userService.getByEmail(email) != null) {
            return badRequest(BadRequestResponseType.EMAIL_ADDRESS_IS_USED);
        }

        return success(email);
    }

    @InitBinder("createBusinessUserForm")
    void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }
}

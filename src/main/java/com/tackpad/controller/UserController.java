package com.tackpad.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tackpad.models.*;
import com.tackpad.models.enums.MessageStatus;
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

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    CompanyAvailableMessageCountService companyAvailableMessageCountService;

    @PostMapping("/business")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = User.class))
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

        user.setCompany(company);

        UserRole userRole = userRoleService.getByUserRole(UserRoleType.ROLE_BUSINESS_USER);
        user.getUserRoles().add(userRole);

        userService.create(user);

        companyBranch.setCompany(company);
        companyBranch.setMain(true);
        companyBranchService.save(companyBranch);

        token.setTokenType(TokenType.TWITTER_AUTH);
        tokenService.save(token);

        CompanyAvailableMessageCount companyAvailableMessageCount = new CompanyAvailableMessageCount();
        companyAvailableMessageCount.setCompany(company);
        companyAvailableMessageCountService.save(companyAvailableMessageCount);
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

    @PutMapping("")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = User.class))
    ResponseEntity update(Authentication authentication,
                          @Validated(User.UpdateUserValidation.class)
                          @RequestBody User user, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.getByEmail(userDetails.getUsername());

        if (!hasRole(UserRoleType.ROLE_ADMIN) && !currentUser.getId().equals(user.getId())) {
            return forbidden(BadRequestResponseType.INVALID_ID);
        }

        User savedUser = userService.getById(user.getId());

        if (!savedUser.getEmail().equals(user.getEmail()) && userService.getByEmail(user.getEmail()) != null) {
            return badRequest(BadRequestResponseType.EMAIL_ADDRESS_IS_USED);
        }

        if (!savedUser.getPhoneNumber().equals(user.getPhoneNumber()) && userService.getByPhoneNumber(user.getPhoneNumber()) != null) {
            return badRequest(BadRequestResponseType.PHONE_NUMBER_IS_USED);
        }

        user.setPassword(savedUser.getPassword());
        userService.merge(user);

        return success(user);
    }

    @GetMapping("/email")
    @ApiResponses(@ApiResponse(code = 200, message = "OK"))
    ResponseEntity checkEmailIsUsed(@RequestParam(value="email") String email) {

        //Unikalnosc meila
        if (userService.getByEmail(email) != null) {
            return badRequest(BadRequestResponseType.EMAIL_ADDRESS_IS_USED);
        }

        return success();
    }

    @GetMapping("/business/me")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = User.class))
    ResponseEntity getMy(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());

        return success(user);
    }

    @GetMapping("/{userId}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = User.class))
    ResponseEntity get(@PathVariable("userId") Long userId) {
        User user = userService.getById(userId);
        return success(user);
    }

    @PostMapping("/diggits")
    ResponseEntity postDigits(@Validated @RequestBody Diggits diggits, Errors errors) throws UnirestException {
       // UserDetails userDetails = (UserDetails) authentication.getPrincipal();
       // User user = userService.getByEmail(userDetails.getUsername());
        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }
        HttpResponse<JsonNode> jsonResponse = Unirest.get(diggits.getUrl())
                .header("Authorization", diggits.getCredentials())
                .asJson();

        JSONObject jsonObject = jsonResponse.getBody().getObject();
        logger.info("Diggits response: {}: " + jsonResponse.getBody());

        DiggitsResponse diggitsResponse = new DiggitsResponse();
        diggitsResponse.setPhoneNumber(jsonObject.getString("phone_number"));
        diggitsResponse.setToken(jsonObject.getJSONObject("access_token").getString("token"));

        return success(diggitsResponse);
    }

    @PutMapping(value = "/password")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = User.class))
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
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = User.class))
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

    @GetMapping(value = "/page/{page}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = CompanyPage.class))
    ResponseEntity getPage(@PathVariable("page") int page,
                           @RequestParam(value = "pageSize", required=false) Integer pageSize,
                           @RequestParam(value = "searchTerm", required=false) String searchTerm) {

        USerPage uSerPage = userService.getPage(page, pageSize, searchTerm);
        return success(uSerPage);
    }

    @DeleteMapping(value = "/{userId}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = Message.class))
    ResponseEntity deleteUser(Authentication authentication, @PathVariable("userId") Long userId) {

        User user = userService.getById(userId);

        if (user == null) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.getByEmail(userDetails.getUsername());

        if (!hasRole(UserRoleType.ROLE_ADMIN) && !currentUser.getId().equals(user.getId())) {
            return forbidden(BadRequestResponseType.INVALID_ID);
        }

        user.setStatus(UserStatus.DELETED);
        userService.merge(user);

        return success(user);
    }
}

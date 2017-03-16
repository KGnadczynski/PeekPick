package com.tackpad.controller;

import com.tackpad.models.Company;
import com.tackpad.models.CompanyBranch;
import com.tackpad.models.CompanyCategory;
import com.tackpad.models.oauth2.User;
import com.tackpad.responses.CompanyPage;
import com.tackpad.responses.Page;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.services.CompanyBranchService;
import com.tackpad.services.CompanyCategoryService;
import com.tackpad.services.CompanyService;
import com.tackpad.services.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Objects;


/**
 * Created by Wojtek on 2016-04-15.
 */
@RestController
@RequestMapping("/companies")
public class CompanyController extends BaseController {

    @Autowired
    public CompanyService companyService;

    @Autowired
    public CompanyCategoryService companyCategoryService;

    @Autowired
    public CompanyBranchService companyBranchService;

    @Autowired
    public UserService userService;

    /**
     * Pobiera strone firm.
     *
     * @param page - strona numer
     * @return @{link ResponseEntity}
     */
    @GetMapping(value = "/page/{page}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = CompanyPage.class))
    ResponseEntity getPage(@PathVariable("page") int page,
                           @RequestParam(value = "pageSize", required=false) Integer pageSize) {

        CompanyPage companyPage = companyService.getPage(page, pageSize);
        return success(companyPage);
    }

    /**
     * Tworzy firme.
     * @param company - firma
     * @return @{link ResponseEntity}
     */
    @PostMapping
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = Company.class))
    ResponseEntity create(@Validated(Company.CreateComapanyValidation.class)  @RequestBody Company company, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        CompanyCategory companyCategory = companyCategoryService.getById(company.getCategory().getId());
        if (companyCategory == null) {
            return badRequest(BadRequestResponseType.INVALID_CATEGORY_ID);
        }

        companyService.save(company);
        return success(company);
    }

    @PutMapping(value = "/{companyId}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = Company.class))
    ResponseEntity update(Authentication authentication, @PathVariable("companyId") Long companyId,
                          @Validated(Company.UpdateComapanyValidation.class) @RequestBody Company company, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());

        if (!Objects.equals(user.getCompany().getId(), companyId)) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        CompanyCategory companyCategory = companyCategoryService.getById(company.getCategory().getId());
        if (companyCategory == null) {
            return badRequest(BadRequestResponseType.INVALID_CATEGORY_ID);
        }

        companyService.save(company);
        
        return success(company);
    }

    /**
     * Pobiera po id.
     * @param companyId - id firmy
     * @return @{link ResponseEntity}
     */
    @GetMapping(value = "/{companyId}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = Company.class))
    ResponseEntity getCompany(@PathVariable("companyId") Long companyId) {

        Company company = companyService.getById(companyId);

        if (company == null) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        return success(company);
    }

    @InitBinder("company")
    void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }
}

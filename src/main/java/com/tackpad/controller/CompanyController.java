package com.tackpad.controller;

import com.tackpad.models.Company;
import com.tackpad.models.CompanyCategory;
import com.tackpad.responses.Page;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.services.CompanyCategoryService;
import com.tackpad.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;


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

    /**
     * Pobiera strone firm.
     *
     * @param page - strona numer
     * @return @{link ResponseEntity}
     */
    @GetMapping(value = "/page/{page}")
    ResponseEntity getPage(@PathVariable("page") int page,
                           @QueryParam("pageSize") Integer pageSize) {

        Page<Company> messagePage = companyService.getPage(page, pageSize);
        return success(messagePage);
    }

    /**
     * Tworzy firme.
     * @param company - firma
     * @return @{link ResponseEntity}
     */
    @PostMapping
    ResponseEntity create(@Validated(Company.CreateComapanyValidation.class)  @RequestBody Company company, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        CompanyCategory companyCategory = companyCategoryService.getById(company.category.id);
        if (companyCategory == null) {
            return badRequest(BadRequestResponseType.INVALID_CATEGORY_ID);
        }

        companyService.save(company);
        return success();
    }

    /**
     * Pobiera po id.
     * @param companyId - id firmy
     * @return @{link ResponseEntity}
     */
    @GetMapping(value = "/companyId/{companyId}")
    ResponseEntity getById(@PathVariable("companyId") Long companyId) {

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

package com.tackpad.controller;

import com.tackpad.models.Company;
import com.tackpad.models.CompanyBranch;
import com.tackpad.models.CompanyCategory;
import com.tackpad.responses.Page;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.services.CompanyBranchService;
import com.tackpad.services.CompanyCategoryService;
import com.tackpad.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;


/**
 * Created by Wojtek on 2016-04-15.
 */
@RestController
@RequestMapping("/companybranches")
public class CompanyBranchController extends BaseController {

    @Autowired
    public CompanyBranchService companyBranchService;

    @GetMapping(value = "/companyId/{companyId}")
    ResponseEntity getListByCompanyId(@PathVariable("companyId") Long companyId) {
        List<CompanyBranch> companyBranchList = companyBranchService.getListByCompanyId(companyId);
        return success(companyBranchList);
    }

}

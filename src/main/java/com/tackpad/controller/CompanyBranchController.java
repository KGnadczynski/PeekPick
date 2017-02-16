package com.tackpad.controller;

import com.tackpad.converters.LongListConverter;
import com.tackpad.converters.MessageTypeListConverter;
import com.tackpad.models.*;
import com.tackpad.models.oauth2.User;
import com.tackpad.requests.enums.ListingSortType;
import com.tackpad.responses.Page;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.services.CompanyBranchService;
import com.tackpad.services.CompanyCategoryService;
import com.tackpad.services.CompanyService;
import com.tackpad.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;


/**
 * Created by Wojtek on 2016-04-15.
 */
@RestController
@RequestMapping("/companybranches")
public class CompanyBranchController extends BaseController {

    @Autowired
    public CompanyBranchService companyBranchService;

    @Autowired
    public CompanyService companyService;

    @Autowired
    public UserService userService;

    @GetMapping(value = "/companyId/{companyId}")
    ResponseEntity getListByCompanyId(@PathVariable("companyId") Long companyId) {
        List<CompanyBranch> companyBranchList = companyBranchService.getListByCompanyId(companyId);
        return success(companyBranchList);
    }

    @GetMapping(value = "/page/{page}")
    ResponseEntity getPage(@PathVariable("page") int page,
                           @QueryParam("pageSize") Integer pageSize,
                           @QueryParam("messageIdList") String messageIdList,
                           @QueryParam("companyBranchId") Long companyBranchId,
                           @QueryParam("companyId") Long companyId,
                           @QueryParam("searchTerm") String searchTerm,
                           @QueryParam("latitude") Double latitude,
                           @QueryParam("longitude") Double longitude,
                           @QueryParam("range") Double range,
                           @QueryParam("sortType") String sortType) {

        LongListConverter longListConverter = new LongListConverter();
        ListingSortType listingSortType = ListingSortType.convertFromString(sortType);

        Page<CompanyBranch> messagePage = null;
        try {
            messagePage = companyBranchService.getPage(page, pageSize,
                    longListConverter.convert(messageIdList),companyBranchId,  companyId, latitude,
                    longitude, range, searchTerm, listingSortType);

            return success(messagePage);
        } catch (ParseException e) {
            return null;
        }

    }

    @GetMapping(value = "/companyId/{companyId}/main")
    ResponseEntity getMainCompanyBranch(@PathVariable("companyId") Long companyId) {

        Company company = companyService.getById(companyId);

        if (company == null) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        CompanyBranch companyBranch = companyBranchService.getMainCompanyBranch(companyId);
        return success(companyBranch);
    }

    @PutMapping(value = "/{companyBranchId}")
    ResponseEntity update(Authentication authentication, @PathVariable("companyBranchId") Long companyBranchId,
                          @Validated(CompanyBranch.UpdateCompanyBranchValidation.class) @RequestBody CompanyBranch companyBranch, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());
        CompanyBranch currentCompanyBranch = companyBranchService.getById(companyBranchId);

        if (currentCompanyBranch == null || !Objects.equals(user.getCompany().getId(), currentCompanyBranch.getCompany().getId())) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        companyBranch.setCompany(user.getCompany());
        companyBranchService.save(companyBranch);

        return success();
    }

}

package com.tackpad.controller;

import com.tackpad.converters.LongListConverter;
import com.tackpad.models.*;
import com.tackpad.models.enums.CompanyBranchStatus;
import com.tackpad.models.oauth2.User;
import com.tackpad.requests.enums.ListingSortType;
import com.tackpad.responses.CompanyBranchPage;
import com.tackpad.responses.Page;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.services.CompanyBranchService;
import com.tackpad.services.CompanyService;
import com.tackpad.services.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = CompanyBranch.class, responseContainer="List"))
    ResponseEntity getListByCompanyId(@PathVariable("companyId") Long companyId) {
        List<CompanyBranch> companyBranchList = companyBranchService.getListByCompanyId(companyId);
        return success(companyBranchList);
    }

    @GetMapping(value = "/page/{page}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = CompanyBranchPage.class))
    ResponseEntity getPage(@PathVariable("page") int page,
                           @RequestParam(value="pageSize", required=false) Integer pageSize,
                           @RequestParam(value="messageIdList", required=false) String messageIdList,
                           @RequestParam(value="companyBranchId", required=false) Long companyBranchId,
                           @RequestParam(value="companyId", required=false) Long companyId,
                           @RequestParam(value="searchTerm", required=false) String searchTerm,
                           @RequestParam(value="latitude", required=false) Double latitude,
                           @RequestParam(value="longitude", required=false) Double longitude,
                           @RequestParam(value="range", required=false) Double range,
                           @RequestParam(value="sortType", required=false) String sortType) {

        LongListConverter longListConverter = new LongListConverter();
        ListingSortType listingSortType = ListingSortType.convertFromString(sortType);

        Page<CompanyBranch> companyBranchPage = null;
        try {
            companyBranchPage = companyBranchService.getPage(page, pageSize,
                    longListConverter.convert(messageIdList),companyBranchId,  companyId, latitude,
                    longitude, range, searchTerm, listingSortType);

            return success(companyBranchPage);
        } catch (ParseException e) {
            return null;
        }

    }

    @GetMapping(value = "/companyId/{companyId}/main")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = CompanyBranch.class))
    ResponseEntity getMainCompanyBranch(@PathVariable("companyId") Long companyId) {

        Company company = companyService.getById(companyId);

        if (company == null) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        CompanyBranch companyBranch = companyBranchService.getMainCompanyBranch(companyId);
        return success(companyBranch);
    }

    @GetMapping(value = "/{companyBranchId}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = CompanyBranch.class))
    ResponseEntity get(@PathVariable("companyBranchId") Long companyBranchId) {

        CompanyBranch companyBranch = companyBranchService.getById(companyBranchId);

        if (companyBranch == null) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        return success(companyBranch);
    }

    @PutMapping(value = "/{companyBranchId}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = CompanyBranch.class))
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

        if (companyBranch.isMain()) {
            CompanyBranch companyBranchMain = companyBranchService.getMainCompanyBranch(user.getCompany().getId());
            companyBranchMain.setMain(false);
            companyBranchService.save(companyBranchMain);
        }

        companyBranch.setCompany(user.getCompany());
        companyBranchService.save(companyBranch);


        return success(companyBranch);
    }

    @PostMapping
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = CompanyBranch.class))
    ResponseEntity create(Authentication authentication,
                          @Validated(CompanyBranch.CreateCompanyBranchValidation.class) @RequestBody CompanyBranch companyBranch, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());

        companyBranch.setCompany(user.getCompany());

        if (companyBranch.isMain()) {
            CompanyBranch companyBranchMain = companyBranchService.getMainCompanyBranch(user.getCompany().getId());
            companyBranchMain.setMain(false);
            companyBranchService.save(companyBranchMain);
        }

        companyBranchService.save(companyBranch);

        return success(companyBranch);
    }

    @DeleteMapping(value = "/{companyBranchId}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = CompanyBranch.class))
    ResponseEntity delete(Authentication authentication, @PathVariable("companyBranchId") Long companyBranchId) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());
        CompanyBranch companyBranch = companyBranchService.getById(companyBranchId);

        if (companyBranch == null || !Objects.equals(user.getCompany().getId(), companyBranch.getCompany().getId())) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        if (companyBranch.isMain()) {
            return badRequest(BadRequestResponseType.CANNOT_DELETE_MAIN_COMPANY_BRANCH);
        }

        companyBranch.setStatus(CompanyBranchStatus.DELETE);
        companyBranchService.save(companyBranch);


        return success(companyBranch);
    }
}

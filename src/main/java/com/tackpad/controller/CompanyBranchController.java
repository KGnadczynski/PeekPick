package com.tackpad.controller;

import com.tackpad.converters.LongListConverter;
import com.tackpad.converters.MessageTypeListConverter;
import com.tackpad.models.*;
import com.tackpad.requests.enums.ListingSortType;
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
import java.text.ParseException;
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

}

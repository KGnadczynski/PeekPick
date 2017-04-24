package com.tackpad.controller;

import com.tackpad.models.BusinessPlan;
import com.tackpad.models.Company;
import com.tackpad.models.CompanyAvailableMessageCount;
import com.tackpad.models.enums.UserRoleType;
import com.tackpad.models.oauth2.User;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.services.BusinessPlanService;
import com.tackpad.services.CompanyAvailableMessageCountService;
import com.tackpad.services.CompanyService;
import com.tackpad.services.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/companyavailablemessagecount")
public class CompanyAvailableMessageCountController extends BaseController {

    @Autowired
    CompanyAvailableMessageCountService companyAvailableMessageCountService;

    @Autowired
    UserService userService;

    @Autowired
    CompanyService companyService;

    @GetMapping("/companyId/{companyId}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = CompanyAvailableMessageCount.class, responseContainer="List"))
    ResponseEntity getByCompanyId(Authentication authentication,  @PathVariable("companyId") Long companyId) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getByEmail(userDetails.getUsername());

        Company company = companyService.getById(companyId);

        if (company == null) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        if (!hasRole(UserRoleType.ROLE_ADMIN) && !company.getId().equals(companyId)) {
            return forbidden(BadRequestResponseType.INVALID_ID);
        }

        return success(companyAvailableMessageCountService.getByCompanyId(companyId));
    }
}

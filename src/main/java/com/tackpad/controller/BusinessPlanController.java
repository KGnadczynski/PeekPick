package com.tackpad.controller;

import com.tackpad.models.BusinessPlan;
import com.tackpad.models.enums.BusinessPlanStatus;
import com.tackpad.responses.enums.BadRequestResponseType;
import com.tackpad.services.BusinessPlanService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/businessplans")
public class BusinessPlanController extends BaseController {

    @Autowired
    BusinessPlanService businessPlanService;

    @GetMapping
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = BusinessPlan.class, responseContainer="List"))
    ResponseEntity getAll() {
        return success(businessPlanService.getAll());
    }

    @PostMapping
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = BusinessPlan.class))
    ResponseEntity create(@Validated  @RequestBody BusinessPlan businessPlan, Errors errors) {

        if (errors.hasErrors()) {
            return badRequest(errors.getAllErrors());
        }

        businessPlanService.save(businessPlan);

        return success(businessPlan);
    }

    @DeleteMapping(value = "/{businessplanId}")
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = BusinessPlan.class))
    ResponseEntity delete(@PathVariable("businessplanId") Long businessplanId) {

        BusinessPlan businessPlan = businessPlanService.getById(businessplanId);

        if (businessPlan  == null) {
            return badRequest(BadRequestResponseType.INVALID_ID);
        }

        businessPlan.setStatus(BusinessPlanStatus.DELETED);
        businessPlanService.save(businessPlan);

        return success(businessPlan);
    }
}

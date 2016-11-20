package com.tackpad.controller;

import com.tackpad.services.CompanyCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comapnycategories")
public class CompanyCategoryController extends BaseController {

    @Autowired
    public CompanyCategoryService companyCategoryService;

    /**
     * Pobiera liste kategorii głównych.
     *
     * @return @{link ResponseEntity}
     */
    @GetMapping(value = "/main")
    ResponseEntity getMainList() {

        return success(companyCategoryService.getMainCategoryList());
    }

    /**
     * Pobiera liste podkategorii.
     *
     * @param parentCategoryId - id podkategorii
     * @return @{link ResponseEntity}
     */
    @GetMapping(value = "/parentCategoryId/{parentCategoryId}")
    ResponseEntity getSubcategoryList(@PathVariable("parentCategoryId") Long parentCategoryId) {

        return success(companyCategoryService.getSubcategoryList(parentCategoryId));
    }

}

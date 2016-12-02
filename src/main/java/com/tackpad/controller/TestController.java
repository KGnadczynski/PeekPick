package com.tackpad.controller;

import com.tackpad.models.Company;
import com.tackpad.models.CompanyBranch;
import com.tackpad.models.CompanyCategory;
import com.tackpad.services.CompanyBranchService;
import com.tackpad.services.CompanyCategoryService;
import com.tackpad.services.CompanyService;
import com.tackpad.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Created by Wojtek on 2016-04-15.
 */
@Controller
@RequestMapping("/test")
public class TestController extends BaseController {

    @Autowired
    public CompanyCategoryService companyCategoryService;

    @Autowired
    public CompanyService companyService;

    @Autowired
    public MessageService messageService;

    @Autowired
    public CompanyBranchService companyBranchService;

    /**
     * Tworzy dane testowe.
     * @return @{link ResponseEntity}
     */
    @GetMapping
    public ResponseEntity create() {

        CompanyCategory companyCategory;
        companyCategory = companyCategoryService.getByName("food truck");
        CompanyCategory companyCategory2 = companyCategoryService.getByName("Escape roomy, parki rozrywki");
        CompanyCategory companyCategory3 = companyCategoryService.getByName("Kino, teatr");

        Company company = new Company();
        company.name = "Najlepsza firma na świecie";
        company.latitude = 54.374269;
        company.longitude = 18.631857;
        company.city = "Gdańsk";
        company.street = "Grunwaldzka";
        company.streetNo = "12d";
        company.category = companyCategory;

        companyService.save(company);

        CompanyBranch companyBranch = new CompanyBranch();
        companyBranch.name = "Oddział 1";
        companyBranch.latitude = 54.374269;
        companyBranch.longitude = 18.631857;
        companyBranch.city = "Gdańsk";
        companyBranch.street = "Grunwaldzka";
        companyBranch.streetNo = "15d";
        companyBranch.company = company;
        companyBranchService.save(companyBranch);

        CompanyBranch companyBranch2 = new CompanyBranch();
        companyBranch2.name = "Oddział 2";
        companyBranch2.latitude = 54.374269;
        companyBranch2.longitude = 18.631857;
        companyBranch2.city = "Gdańsk";
        companyBranch2.street = "Grunwaldzka";
        companyBranch2.streetNo = "12d";
        companyBranch2.company = company;
        companyBranchService.save(companyBranch2);

        CompanyBranch companyBranch3 = new CompanyBranch();
        companyBranch3.name = "Oddział 5 Firemki";
        companyBranch3.latitude = 52.374269;
        companyBranch3.longitude = 18.931857;
        companyBranch3.city = "Gdańsk";
        companyBranch3.street = "Grunwaldzka";
        companyBranch3.streetNo = "12d";
        companyBranch3.company = company;
        companyBranchService.save(companyBranch3);

        return success();
    }
}

package com.endava.service_system.controller;

import com.endava.service_system.dto.CompanyRegistrationDTO;
import com.endava.service_system.model.Company;
import com.endava.service_system.service.CompanyService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class CompanyRestController {

//TODO : access-rights on each method

    private final CompanyService companyService;

    public CompanyRestController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    public ResponseEntity getAllCompanies(Model model){
        List<Company> companies = companyService.getAllCompanies();
        return new ResponseEntity(companies, HttpStatus.OK);
    }

    @GetMapping("/companies/{companyName}")
    public ResponseEntity getCompanyByName(@PathVariable("companyName") String companyName){
        Optional<Company> company = companyService.getCompanyByName(companyName);
        if (company.isPresent())
            return new ResponseEntity(company.get(), HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}

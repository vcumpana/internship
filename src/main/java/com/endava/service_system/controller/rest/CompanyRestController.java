package com.endava.service_system.controller.rest;

import com.endava.service_system.dto.CompanyAdminDTO;
import com.endava.service_system.dto.CompanyRegistrationDTO;
import com.endava.service_system.dto.CredentialDTO;
import com.endava.service_system.dto.UserAdminDTO;
import com.endava.service_system.enums.UserStatus;
import com.endava.service_system.model.Company;
import com.endava.service_system.model.Credential;
import com.endava.service_system.service.BankService;
import com.endava.service_system.service.CompanyService;
import com.endava.service_system.service.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CompanyRestController {

//TODO : access-rights on each method

    private CompanyService companyService;
    private ConversionService conversionService;
    private CredentialService credentialService;
    private BankService bankService;

    @GetMapping("/admin/companies")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity getAllCompanies(@RequestParam(required = false,value = "status")UserStatus status) {
        List<Company> companyList=getCompaniesWithStatus(status);
        List<CompanyAdminDTO> companyAdminDTOList=companyList.stream()
                .map((company)->conversionService.convert(company,CompanyAdminDTO.class))
                .collect(Collectors.toList());
        if(!companyAdminDTOList.isEmpty()) {
            return new ResponseEntity(companyAdminDTOList, HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    private List<Company> getCompaniesWithStatus(UserStatus status){
        List<Company> companyList=new ArrayList<>();
        if(status==null){
            companyList.addAll(companyService.getAll());
        }else{
            companyList.addAll(companyService.getAllWithStatus(status));
        }
        return companyList;
    }

    @PutMapping("/admin/companies/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity changePasswordWithStatus(@PathVariable("username") String username,
                                                   @Validated @RequestBody CredentialDTO credentialDTO,
                                                   BindingResult bindingResult) {
        System.out.println("username:"+username);
        System.out.println("CredentialDTO:"+credentialDTO.toString());
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try {
            if(credentialDTO.getStatus()==UserStatus.ACCEPTED){
                Optional<Credential> companyCredentials=credentialService.getByUsername(username);
                if(!companyCredentials.isPresent()){
                    String json = "{\"message\":\"Bank Problem\"}";
                    return new ResponseEntity(json, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                bankService.addBankAccount(companyCredentials.get());
            }
            int entitiesUpdated = credentialService.updateStatusAndPassword(username, credentialDTO);
            String json = "{\"count\":\"1\"}";
            return new ResponseEntity(json, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/companies/{companyName}")
    public ResponseEntity getCompanyByName(@PathVariable("companyName") String companyName){
        Optional<Company> company = companyService.getCompanyByName(companyName);
        if (company.isPresent())
            return new ResponseEntity(company.get(), HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Autowired
    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @Autowired
    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }
}

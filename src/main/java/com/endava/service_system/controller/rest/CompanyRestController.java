package com.endava.service_system.controller.rest;

import com.endava.service_system.model.dto.CompanyAdminDTO;
import com.endava.service_system.model.dto.CredentialDTO;
import com.endava.service_system.model.entities.Company;
import com.endava.service_system.model.entities.Credential;
import com.endava.service_system.model.enums.UserStatus;
import com.endava.service_system.service.BankService;
import com.endava.service_system.service.CompanyService;
import com.endava.service_system.service.CredentialService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CompanyRestController {
    private static final Logger LOGGER=LogManager.getLogger(CompanyRestController.class);
    private static final int DEFAUL_COMPANY_SIZE = 5;
    private CompanyService companyService;
    private ConversionService conversionService;
    private CredentialService credentialService;
    private BankService bankService;

    @GetMapping("/admin/companies")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity getAllCompanies(@RequestParam(required = false,value = "status")UserStatus status,@RequestParam(required = false,value = "page")Integer page) {
        if(page==null||page<0){
            page=0;
        }
        Map<String,Object> result= companyService.getAll(PageRequest.of(page,DEFAUL_COMPANY_SIZE),status);
        List<Company> companies = (List<Company>) result.get("companies");
        List<CompanyAdminDTO> companiesForAdmin = companies.stream()
                .map((user) -> conversionService.convert(user, CompanyAdminDTO.class))
                .collect(Collectors.toList());
        result.put("companies",companiesForAdmin);
        LOGGER.debug(result);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PutMapping("/admin/companies/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity changePasswordWithStatus(@PathVariable("username") String username,
                                                   @Validated @RequestBody CredentialDTO credentialDTO,
                                                   BindingResult bindingResult) {
        LOGGER.debug("username:"+username);
        LOGGER.debug("CredentialDTO:"+credentialDTO.toString());
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

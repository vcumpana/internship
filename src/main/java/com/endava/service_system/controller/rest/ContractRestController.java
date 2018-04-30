package com.endava.service_system.controller.rest;

import com.endava.service_system.dto.ContractDtoFromUser;
import com.endava.service_system.service.CompanyService;
import com.endava.service_system.service.ContractService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContractRestController {

    private ContractService contractService;

    public ContractRestController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping("/newContract")
    public ResponseEntity newContract(@RequestBody ContractDtoFromUser contractDto){
        if (contractService.saveContract(contractDto) == null)
            return new ResponseEntity(HttpStatus.CONFLICT);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}

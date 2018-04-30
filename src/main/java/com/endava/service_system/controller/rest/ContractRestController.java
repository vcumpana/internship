package com.endava.service_system.controller.rest;

import com.endava.service_system.dto.ContractDtoFromUser;
import com.endava.service_system.dto.ContractToUserDto;
import com.endava.service_system.dto.ServiceToUserDto;
import com.endava.service_system.enums.ContractStatus;
import com.endava.service_system.model.ContractForUserDtoFilter;
import com.endava.service_system.model.ServiceDtoFilter;
import com.endava.service_system.service.CompanyService;
import com.endava.service_system.service.ContractService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("user/contracts")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<ContractToUserDto> getUserContracts(@RequestParam(value = "categoryId",required = false) Integer categoryId,
                                                    @RequestParam(value = "size",required = false) Integer size,
                                                    @RequestParam(value = "page",required = false) Integer page,
                                                    @RequestParam(value = "status",required = false) ContractStatus contractStatus,
                                                    @RequestParam(required = false,value = "companyId") Integer companyId,
                                                    @RequestParam(required = false,value = "company") String companyName,
                                                    @RequestParam(required = false,value = "category") String categoryName,
                                                    @RequestParam(required = false,value = "orderByEndDate")String order,
                                                    Authentication authentication){
        String username=authentication.getName();
        Sort.Direction direction=getDirection(order);
        ContractForUserDtoFilter filter=ContractForUserDtoFilter.builder()
                .username(username)
                .size(size)
                .direction(direction)
                .categoryId(categoryId)
                .companyId(companyId)
                .contractStatus(contractStatus)
                .categoryName(categoryName)
                .companyName(companyName)
                .page(page)
                .build();
        return contractService.getUserContracts(filter);
    }

    //TODO refactore Services has same method
    private Sort.Direction getDirection(String order){
        Sort.Direction direction;
        if(order==null){
            direction=null;
        }else if(order.equalsIgnoreCase("asc")) {
            direction =Sort.Direction.ASC;
        }else if(order.equalsIgnoreCase("desc")){
            direction =Sort.Direction.DESC;
        }else{
            direction=null;
        }
        return direction;
    }
}

package com.endava.service_system.controller.rest;

import com.endava.service_system.dto.ContractDtoFromUser;
import com.endava.service_system.dto.ContractForShowingDto;
import com.endava.service_system.enums.ContractStatus;
import com.endava.service_system.enums.UserType;
import com.endava.service_system.model.ContractForUserDtoFilter;
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

    @GetMapping(value={"user/contracts","company/contracts"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<ContractForShowingDto> getUserContracts(@RequestParam(value = "categoryId",required = false) Long categoryId,
                                                        @RequestParam(value = "size",required = false) Integer size,
                                                        @RequestParam(value = "page",required = false) Integer page,
                                                        @RequestParam(value = "status",required = false) ContractStatus contractStatus,
                                                        @RequestParam(required = false,value = "companyId") Long companyId,
                                                        @RequestParam(required = false,value = "company") String companyName,
                                                        @RequestParam(required = false,value = "category") String categoryName,
                                                        @RequestParam(required = false,value = "orderByEndDate")String order,
                                                        Authentication authentication){
        String username=authentication.getName();
        Sort.Direction direction=getDirection(order);
        UserType userType=getUserType(authentication);
        ContractForUserDtoFilter filter=ContractForUserDtoFilter.builder()
                .username(username)
                .size(size)
                .direction(direction)
                .categoryId(categoryId)
                .companyId(companyId)
                .contractStatus(contractStatus)
                .userType(userType)
                .categoryName(categoryName)
                .companyName(companyName)
                .page(page)
                .build();
        return contractService.getContracts(filter);
    }

    private UserType getUserType(Authentication authentication){
        UserType userType;
        String authority=authentication.getAuthorities().stream().findAny().get().getAuthority();
        if(authority.equalsIgnoreCase("ROLE_COMPANY")){
            userType=UserType.COMPANY;
        }else{
            userType=UserType.USER;
        }
        return userType;
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

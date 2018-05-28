package com.endava.service_system.controller.rest;

import com.endava.service_system.model.dto.ContractDtoFromUser;
import com.endava.service_system.model.dto.ContractForShowingDto;
import com.endava.service_system.model.entities.Contract;
import com.endava.service_system.model.enums.ContractStatus;
import com.endava.service_system.model.enums.UserType;
import com.endava.service_system.model.filters.ContractForUserDtoFilter;
import com.endava.service_system.model.filters.order.ContractOrderBy;
import com.endava.service_system.service.ContractService;
import com.endava.service_system.service.InvoiceService;
import com.endava.service_system.service.NotificationService;
import com.endava.service_system.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.endava.service_system.model.enums.ContractStatus.ACTIVE;
import static com.endava.service_system.model.enums.ContractStatus.DENIED;

@RequiredArgsConstructor
@RestController
public class ContractRestController {

    private final ContractService contractService;
    private final InvoiceService invoiceService;
    private final AuthUtils authUtils;
    private final NotificationService notificationService;

    @PostMapping("/newContract")
    public ResponseEntity newContract(@RequestBody ContractDtoFromUser contractDto) {
        if (contractService.saveContract(contractDto) != null) {
            notificationService.saveAboutContractFromUser(contractDto);
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @GetMapping(value = {"user/contracts", "company/contracts"})
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_COMPANY')")
    public Map<String,Object> getUserContracts(@RequestParam(value = "categoryId",required = false) Long categoryId,
                                               @RequestParam(value = "size",required = false) Integer size,
                                               @RequestParam(value = "page",required = false) Integer page,
                                               @RequestParam(value = "status",required = false) ContractStatus contractStatus,
                                               @RequestParam(required = false,value = "companyId") Long companyId,
                                               @RequestParam(required = false,value = "company") String companyName,
                                               @RequestParam(required = false,value = "category") String categoryName,
                                               @RequestParam(required = false,value = "order")String order,
                                               @RequestParam(required = false,value = "orderBy")ContractOrderBy orderBy,
                                               @RequestParam(required = false,value = "usersFirstName") String usersFirstName,
                                               @RequestParam(required = false,value = "usersLastName") String usersLastName,
                                               @RequestParam(required = false,value = "fromStartDate") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                           LocalDate fromStartDate,
                                               @RequestParam(required = false,value = "tillStartDate") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                           LocalDate tillStartDate,
                                               @RequestParam(required = false,value = "fromEndDate") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                           LocalDate fromEndDate,
                                               @RequestParam(required = false,value = "tillEndDate") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                           LocalDate tillEndDate,
                                               @RequestParam(required = false,value = "serviceId") Long serviceId,
                                               Authentication authentication){
        Map<String,Object> map=new HashMap<>();
        String username=authentication.getName();
        Sort.Direction direction=getDirection(order);
        UserType userType=getUserType(authentication);
        ContractForUserDtoFilter filter=ContractForUserDtoFilter.builder()
                .username(username)
                .size(size)
                .order(direction)
                .orderBy(orderBy)
                .categoryId(categoryId)
                .companyId(companyId)
                .contractStatus(contractStatus)
                .userType(userType)
                .categoryName(categoryName)
                .companyName(companyName)
                .usersFirstName(usersFirstName)
                .usersLastName(usersLastName)
                .fromEndDate(fromEndDate)
                .tillEndDate(tillEndDate)
                .fromStartDate(fromStartDate)
                .tillStartDate(tillStartDate)
                .page(page)
                .serviceId(serviceId)
                .build();
        List<ContractForShowingDto> list = contractService.getContracts(filter);
        list = invoiceService.setReadinessForInvoiceCreation(list);
        map.put("contracts",list);
        map.put("pages",contractService.getPagesSizeForFilter(filter));
        return map;
    }

    @GetMapping("/contract/{id}/{action}")
    public ModelAndView changeContractStatus(@PathVariable("id") Long contractId, @PathVariable("action") String action,
                                             HttpServletRequest request){
        Contract contract = contractService.getContractById(contractId);
        if (contract != null) {
            if(contract.getCompany().getCredential().getUsername().equals((authUtils.getAuthenticatedUsername()))){
                if(action.toLowerCase().equals("approve"))
                    contract.setStatus(ACTIVE);
                else if(action.toLowerCase().equals("deny"))
                    contract.setStatus(DENIED);
                notificationService.saveAboutContractFromCompany(contract);
                contractService.update(contract);
            }
        }
        String referer = request.getHeader("Referer");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:"+ referer);
        return modelAndView;
    }

    private UserType getUserType(Authentication authentication) {
        UserType userType;
        String authority = authentication.getAuthorities().stream().findAny().get().getAuthority();
        if (authority.equalsIgnoreCase("ROLE_COMPANY")) {
            userType = UserType.COMPANY;
        } else {
            userType = UserType.USER;
        }
        return userType;
    }

    //TODO refactore Services has same method
    private Sort.Direction getDirection(String order) {
        Sort.Direction direction;
        if (order == null) {
            direction = null;
        } else if (order.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        } else if (order.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        } else {
            direction = null;
        }
        return direction;
    }

    @GetMapping(value = "company/contracts/allIds")
    public ResponseEntity getAllContractsIds(){
        Long [] contractsIds = contractService.getAllContractsIdsByCompanyUsername(authUtils.getAuthenticatedUsername());
        return new ResponseEntity(contractsIds, HttpStatus.OK);
    }
}

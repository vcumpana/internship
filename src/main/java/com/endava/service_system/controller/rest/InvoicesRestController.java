package com.endava.service_system.controller.rest;

import com.endava.service_system.enums.ContractStatus;
import com.endava.service_system.enums.InvoiceStatus;
import com.endava.service_system.enums.UserType;
import com.endava.service_system.model.InvoiceFilter;
import com.endava.service_system.model.User;
import com.endava.service_system.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InvoicesRestController {

    private InvoiceService invoiceService;

    @GetMapping("/invoices")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_COMPANY')")
    public List getInvoices(Authentication authentication,
                            @RequestParam(value = "categoryId",required = false) Long categoryId,
                            @RequestParam(value = "size",required = false) Integer size,
                            @RequestParam(value = "page",required = false) Integer page,
                            @RequestParam(value = "status",required = false) InvoiceStatus status,
                            @RequestParam(required = false,value = "companyId") Long companyId,
                            @RequestParam(required = false,value = "company") String companyName,
                            @RequestParam(required = false,value = "category") String categoryName,
                            @RequestParam(required = false,value = "orderByDueDate")String order
                            ){
        UserType userType=getUserType(authentication);
        String username=authentication.getName();
        InvoiceFilter filter=InvoiceFilter.builder()
                .userType(userType)
                .currentUserUsername(username)
                .size(size)
                .orderByDueDateDirection(getDirection(order))
                .categoryId(categoryId)
                .companyId(companyId)
                .categoryName(categoryName)
                .companyTitle(companyName)
                .invoiceStatus(status)
                .page(page)
                .build();
        return invoiceService.getAllInvoices(filter);
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

    @Autowired
    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

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

}

package com.endava.service_system.controller.rest;

import com.endava.service_system.dto.NewInvoiceDTO;
import com.endava.service_system.enums.ContractStatus;
import com.endava.service_system.enums.InvoiceStatus;
import com.endava.service_system.enums.UserType;
import com.endava.service_system.model.Invoice;
import com.endava.service_system.model.InvoiceFilter;
import com.endava.service_system.model.Service;
import com.endava.service_system.model.User;
import com.endava.service_system.service.InvoiceService;
import com.endava.service_system.utils.AuthUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.endava.service_system.enums.UserStatus.DENIED;

@RestController
public class InvoicesRestController {

    private static final Logger LOGGER= LogManager.getLogger(InvoicesRestController.class);
    private InvoiceService invoiceService;
    private AuthUtils authUtils;

    @Autowired
    public void setAuthUtils(AuthUtils authUtils) {
        this.authUtils = authUtils;
    }
    @Autowired
    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
    @GetMapping("/invoices")
    public Map<String,Object> getInvoices(Authentication authentication,
                                          @RequestParam(value = "categoryId",required = false) Long categoryId,
                                          @RequestParam(value = "size",required = false) Integer size,
                                          @RequestParam(value = "page",required = false) Integer page,
                                          @RequestParam(value = "status",required = false) InvoiceStatus status,
                                          @RequestParam(required = false,value = "companyId") Long companyId,
                                          @RequestParam(required = false,value = "company") String companyName,
                                          @RequestParam(required = false,value = "category") String categoryName,
                                          @RequestParam(required = false,value = "orderByDueDate")String order
                            ){
        LOGGER.log(Level.DEBUG,authentication);
        Map<String,Object> result=new HashMap<>();
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
        result.put("invoices",invoiceService.getAllInvoices(filter));
        result.put("pages",invoiceService.getInvoicePagesNr(filter));
        return result;
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

    @PostMapping("/company/newinvoices")
    @PreAuthorize("hasAnyRole('ROLE_COMPANY')")
    public ResponseEntity createMultipleInvoices(@RequestParam(value = "info[]", required=false) List<String> ids){
       invoiceService.createInvoicesFromBulk(ids);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/company/sendinvoices")
    @PreAuthorize("hasAnyRole('ROLE_COMPANY')")
    public ResponseEntity sendMultipleInvoices(@RequestParam(value = "info[]", required=false) List<String> ids){
        invoiceService.sendInvoicesFromBulk(ids);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/company/cancelinvoices")
    @PreAuthorize("hasAnyRole('ROLE_COMPANY')")
    public ResponseEntity cancelMultipleInvoices(@RequestParam(value = "info[]", required=false) List<String> ids){
        invoiceService.cancelInvoicesFromBulk(ids);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/invoice/{id}/{action}")
    public ResponseEntity sendOrCancelInvoice(@PathVariable("id") Long invoiceId, @PathVariable("action") String action,
                                             HttpServletRequest request, Model model) {
        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        if (invoice != null) {
            if (invoice.getContract().getCompany().getCredential().getUsername().equals((authUtils.getAuthenticatedUsername()))) {
                if (action.toLowerCase().equals("send")) {
                    invoice.setInvoiceStatus(InvoiceStatus.SENT);
                    invoiceService.update(invoice);
                    return new ResponseEntity("Invoice has been sent succsesfully", HttpStatus.OK);
                } else if (action.toLowerCase().equals("cancel")) {
                    invoiceService.deleteInvoice(invoice);
                    return new ResponseEntity("Invoice has been canceled succsesfully", HttpStatus.OK);
                }

            }
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/invoice/{id}/edit")
    public String editInvoice(@PathVariable("id") Long invoiceId, @PathVariable("action") String action,
                              HttpServletRequest request, Model model) {
        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        if (invoice != null) {
            if (invoice.getContract().getCompany().getCredential().getUsername().equals((authUtils.getAuthenticatedUsername()))) {
                if (action.toLowerCase().equals("edit")) {
                    NewInvoiceDTO newInvoiceDTO = new NewInvoiceDTO();
                    newInvoiceDTO.setContractId(invoice.getContract().getId());
                    newInvoiceDTO.setDueDate(invoice.getDueDate());
                    newInvoiceDTO.setPrice(invoice.getPrice());
                    newInvoiceDTO.setFromDate(invoice.getFromDate());
                    newInvoiceDTO.setTillDate(invoice.getTillDate());
                    model.addAttribute("invoice", newInvoiceDTO);
                    return "companyCreateInvoice";
                }
            }
        }
        return "redirect:/company/invoices";
    }
}

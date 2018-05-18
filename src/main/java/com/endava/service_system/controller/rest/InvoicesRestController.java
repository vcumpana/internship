package com.endava.service_system.controller.rest;

import com.endava.service_system.model.dto.NewInvoiceDTO;
import com.endava.service_system.model.filters.order.InvoiceOrderBy;
import com.endava.service_system.model.enums.InvoiceStatus;
import com.endava.service_system.model.enums.UserType;
import com.endava.service_system.model.entities.Invoice;
import com.endava.service_system.model.filters.InvoiceFilter;
import com.endava.service_system.service.InvoiceService;
import com.endava.service_system.utils.AuthUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class InvoicesRestController {

    private static final Logger LOGGER = LogManager.getLogger(InvoicesRestController.class);
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
    public Map<String, Object> getInvoices(Authentication authentication,
                                           @RequestParam(value = "categoryId", required = false) Long categoryId,
                                           @RequestParam(value = "size", required = false) Integer size,
                                           @RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(value = "status", required = false) InvoiceStatus status,
                                           @RequestParam(required = false, value = "companyId") Long companyId,
                                           @RequestParam(required = false, value = "company") String companyName,
                                           @RequestParam(required = false, value = "category") String categoryName,
                                           @RequestParam(required = false, value = "order") String order,
                                           @RequestParam(required = false,value = "orderBy") InvoiceOrderBy orderBy,
                                           @RequestParam(required = false,value = "contractNr") Long contractId,
                                           @RequestParam(required = false, value = "fromStartDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromStartDate,
                                           @RequestParam(required = false, value = "tillStartDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate tillStartDate,
                                           @RequestParam(required = false, value = "fromTillDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromTillDate,
                                           @RequestParam(required = false, value = "tillTillDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate tillTillDate,
                                           @RequestParam(required = false, value = "fromDueDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDueDate,
                                           @RequestParam(required = false, value = "tillDueDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate tillDueDate,
                                           @RequestParam(required = false, value = "usersFirstName") String usersFirstName,
                                           @RequestParam(required = false, value = "usersLastName") String usersLastName,
                                           @RequestParam(required = false,value = "serviceId") Long serviceId
    ) {
        LOGGER.log(Level.DEBUG, authentication);
        Map<String, Object> result = new HashMap<>();
        UserType userType = getUserType(authentication);
        String username = authentication.getName();
        InvoiceFilter filter = InvoiceFilter.builder()
                .userType(userType)
                .currentUserUsername(username)
                .size(size)
                .orderDirection(getDirection(order))
                .categoryId(categoryId)
                .companyId(companyId)
                .categoryName(categoryName)
                .orderBy(orderBy)
                .companyTitle(companyName)
                .invoiceStatus(status)
                .page(page)
                .usersLastName(usersLastName)
                .usersFirstName(usersFirstName)
                .fromStartDate(fromStartDate)
                .tillStartDate(tillStartDate)
                .fromTillDate(fromTillDate)
                .tillTillDate(tillTillDate)
                .fromDueDate(fromDueDate)
                .tillDueDate(tillDueDate)
                .contractId(contractId)
                .serviceId(serviceId)
                .build();
        result.put("invoices", invoiceService.getAllInvoices(filter));
        result.put("pages", invoiceService.getInvoicePagesNr(filter));
        return result;
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
    public ResponseEntity createMultipleInvoices(@RequestParam(value = "info[]", required = false) List<String> ids) {
        if (ids == null || ids.size() == 0)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        Map<String, Integer> report = invoiceService.createInvoicesFromBulk(ids);
        return new ResponseEntity(report, HttpStatus.CREATED);
    }

    @PostMapping("/company/sendinvoices")
    @PreAuthorize("hasAnyRole('ROLE_COMPANY')")
    public ResponseEntity sendMultipleInvoices(@RequestParam(value = "info[]", required = false) List<String> ids) {
        if (ids == null || ids.size() == 0)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        invoiceService.sendInvoicesFromBulk(ids);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/company/cancelinvoices")
    @PreAuthorize("hasAnyRole('ROLE_COMPANY')")
    public ResponseEntity cancelMultipleInvoices(@RequestParam(value = "info[]", required = false) List<String> ids) {
        if (ids == null || ids.size() == 0)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
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
                    invoiceService.sendInvoice(invoice);
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
    public ModelAndView editInvoice(@PathVariable("id") Long invoiceId, HttpServletRequest request, Model model) {
        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        ModelAndView modelAndView = new ModelAndView();
        if (invoice != null) {
            if (invoice.getContract().getCompany().getCredential().getUsername().equals((authUtils.getAuthenticatedUsername()))) {
                NewInvoiceDTO newInvoiceDTO = new NewInvoiceDTO();
                newInvoiceDTO.setContractId(invoice.getContract().getId());
                newInvoiceDTO.setDueDate(invoice.getDueDate());
                newInvoiceDTO.setPrice(invoice.getPrice());
                newInvoiceDTO.setFromDate(invoice.getFromDate());
                newInvoiceDTO.setTillDate(invoice.getTillDate());
                newInvoiceDTO.setInvoiceId(invoice.getId());
                newInvoiceDTO.setService(invoice.getContract().getService().getTitle());
                newInvoiceDTO.setClientName(invoice.getContract().getUser().getName() + " " + invoice.getContract().getUser().getSurname());
                modelAndView.setViewName("companyEditInvoice");
                modelAndView.addObject("invoice", newInvoiceDTO);
                return modelAndView;
            }
        }
        modelAndView.setViewName("redirect:/company/invoices");
        return modelAndView;
    }
}

package com.endava.service_system.controller;

import com.endava.service_system.model.dto.CompanyDtoToShow;
import com.endava.service_system.model.dto.CompanyRegistrationDTO;
import com.endava.service_system.model.dto.NewInvoiceDTO;
import com.endava.service_system.model.dto.NewServiceDTO;
import com.endava.service_system.model.entities.*;
import com.endava.service_system.service.*;
import com.endava.service_system.utils.AuthUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Controller
public class CompanyController {

    private static Logger LOGGER = LogManager.getLogger(CompanyController.class);
    private CompanyService companyService;
    private ConversionService conversionService;
    private CategoryService categoryService;
    private ServiceService serviceService;
    private ContractService contractService;
    private InvoiceService invoiceService;
    private AuthUtils authUtils;

    @GetMapping("/company/registration")
    public String getCompanyRegistrationForm(Model model) {
        model.addAttribute("company", new CompanyRegistrationDTO());

        return "companyRegistration";
    }

    @PostMapping("/company/registration")
    public String registerCompany(Model model, @ModelAttribute("company") @Valid CompanyRegistrationDTO companyRegistrationDTO, BindingResult bindingResult) {
        LOGGER.log(Level.DEBUG, companyRegistrationDTO);

        if (bindingResult.hasErrors()) {
            model.addAttribute("company", companyRegistrationDTO);
            return "companyRegistration";
        }
        companyService.save(conversionService.convert(companyRegistrationDTO, Company.class));
        return "redirect:/login";
    }

    @GetMapping(value = "/company/cabinet")
    public String companyCabinet(Model model) {
        String username = authUtils.getAuthenticatedUsername();
        CompanyDtoToShow company = conversionService.convert(companyService.getCompanyByUsername(username).get(), CompanyDtoToShow.class);
        addCompanyNameToModel(model);
        model.addAttribute("user", company);
        model.addAttribute("username1", username);
        addCompanyToModel(model);
        return "companyCabinet";
    }

    @GetMapping(value = "/company/serviceList")
    public String userServiceList(Model model) {
        addCompanyNameToModel(model);
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        return "companyServiceList";
    }

    @GetMapping(value = "/company/mycontracts")
    public String getMyContractsPage(Model model) {
        addCompanyNameToModel(model);
        addCategoriesToModel(model);
        addServicesToModel(model);
        return "companyContractList";
    }

    @GetMapping(value = "/company/notifications")
    public String getMyNotificationsPage(Model model) {
        addCompanyNameToModel(model);
        return "companyNotifications";
    }

    @GetMapping(value = "/company/myinvoices")
    public String getMyInvoicesPage(Model model) {
        addCompanyNameToModel(model);
        addCategoriesToModel(model);
        addServicesToModel(model);
        return "companyInvoices";
    }

    @GetMapping(value = "/company/statements")
    public String userStatements(Model model) {
        addCompanyNameToModel(model);
        return "companyStatement";
    }

    @GetMapping("/company/addservice")
    public String getCompanyAddServiceForm(Model model) {
        model.addAttribute("service", new NewServiceDTO());
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        addCompanyNameToModel(model);
        return "companyAddService";
    }

    @GetMapping("/service/{id}/edit")
    public String editInvoice(@PathVariable("id") Long serviceId, HttpServletRequest request, Model model) {
        Service service = serviceService.getServiceById(serviceId).get();
        NewServiceDTO serviceDTO = new NewServiceDTO();
        serviceDTO.setCategory(service.getCategory().getName());
        serviceDTO.setDescription(service.getDescription());
        serviceDTO.setTitle(service.getTitle());
        serviceDTO.setPrice(service.getPrice());
        serviceDTO.setId(service.getId());
        Optional<Company> company = companyService.getCompanyNameByUsername(authUtils.getAuthenticatedUsername());
        if (serviceService.getServicesByCompanyName(company.get().getName()).contains(service)) {
            List<Category> categories = categoryService.getAll();
            model.addAttribute("categories", categories);
            model.addAttribute("username", company.get().getName());
            model.addAttribute("service", serviceDTO);
            LOGGER.debug(serviceDTO);
            return "companyEditService";
        }
        return "redirect:/company/serviceList";
    }

    @PostMapping("/company/addservice")
    public String registerNewService(Model model, @ModelAttribute("service") @Valid NewServiceDTO newServiceDTO, BindingResult bindingResult) {
        LOGGER.debug(newServiceDTO);
        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryService.getAll();
            model.addAttribute("categories", categories);
            model.addAttribute("service", newServiceDTO);
            return "companyAddService";
        }
        Service service = serviceService.save(conversionService.convert(newServiceDTO, Service.class));
        companyService.addNewService(service);
        return "redirect:/company/serviceList";
    }

    @PostMapping("/company/updateService")
    public String updateService(Model model, @ModelAttribute("service") @Valid NewServiceDTO newServiceDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        LOGGER.debug(newServiceDTO);
        if (bindingResult.hasErrors()) {
            List<Category> categories = categoryService.getAll();
            model.addAttribute("categories", categories);
            model.addAttribute("service", newServiceDTO);
            return "companyAddService";
        }
        Service beforeSaving = conversionService.convert(newServiceDTO, Service.class);
        beforeSaving.setId(newServiceDTO.getId());
        companyService.updateService(beforeSaving);
        redirectAttributes.addFlashAttribute("message", "Service has been successfully updated");
        return "redirect:/company/serviceList";
    }

    @GetMapping("/contract/{id}/createInvoice")
    public String getCompanyCreateInvoiceForm(Model model, @PathVariable("id") Long contractId) {
        NewInvoiceDTO newInvoiceDTO = invoiceService.getInvoiceDtoByContractId(contractId);
        model.addAttribute("invoice", newInvoiceDTO);
        addCompanyNameToModel(model);
        return "companyCreateInvoice";
    }

    @PostMapping("/company/createInvoice")
    public String registerNewService(RedirectAttributes redirectAttributes, HttpServletResponse response, HttpServletRequest request, Model model, @ModelAttribute("invoice") @Validated NewInvoiceDTO newInvoiceDTO, BindingResult bindingResult) {
        LOGGER.log(Level.DEBUG, newInvoiceDTO);
        LOGGER.log(Level.DEBUG, bindingResult.getAllErrors());
        if (bindingResult.hasErrors()) {
            model.addAttribute("invoice", newInvoiceDTO);
            for (Cookie one : request.getCookies()) {
                if (one.getName().equalsIgnoreCase("conflict")) {
                    one.setValue("");
                    one.setPath("/");
                    one.setMaxAge(0);
                    response.addCookie(one);
                }
            }
            return "companyCreateInvoice";
        }

        //i
        if (invoiceService.invoicePeriodExists(newInvoiceDTO)) {
            String cookieValue = null;
            for (Cookie one : request.getCookies()) {
                if (one.getName().equalsIgnoreCase("conflict")) {
                    cookieValue = one.getValue();
                }
            }
            LOGGER.debug("cookieValue: " + cookieValue);
            if (cookieValue == null || cookieValue.equalsIgnoreCase("true")) {
                LOGGER.debug("adding cookie : ");
                Cookie conflictCookie = new Cookie("conflict", "true");
                response.addCookie(conflictCookie);
                return "companyCreateInvoice";
            } else {

                for (Cookie one : request.getCookies()) {
                    if (one.getName().equalsIgnoreCase("conflict")) {
                        one.setValue("");
                        one.setPath("/");
                        one.setMaxAge(0);
                        response.addCookie(one);
                    }
                }
            }
        }
        LOGGER.debug("there are no conflicts : ");
        Contract contract = contractService.getContractById(newInvoiceDTO.getContractId());
        Invoice invoice = conversionService.convert(newInvoiceDTO, Invoice.class);
        invoice.setContract(contract);
        invoice.setCreatedDate(LocalDate.now());
        invoiceService.save(invoice);
        User user = contract.getUser();
        String fullName = user.getName() + " " + user.getSurname();
        String serviceTitle = contract.getService().getTitle();
        redirectAttributes.addFlashAttribute("message", "You have created an invoice for client: " + fullName + ", on service : " + serviceTitle + " with sum " + invoice.getPrice() + " USD");
        return "redirect:/company/myinvoices";
    }

    @PostMapping("/company/editInvoice")
    public String editInvoice(Model model, @ModelAttribute("invoice") @Validated NewInvoiceDTO newInvoiceDTO, BindingResult bindingResult) {
        LOGGER.log(Level.DEBUG, newInvoiceDTO);
        LOGGER.log(Level.DEBUG, bindingResult.getAllErrors());
        if (bindingResult.hasErrors()) {
            model.addAttribute("invoice", newInvoiceDTO);
            return "companyEditInvoice";
        }
        Contract contract = contractService.getContractById(newInvoiceDTO.getContractId());
        Invoice invoice = conversionService.convert(newInvoiceDTO, Invoice.class);
        invoice.setContract(contract);
        invoiceService.save(invoice);
        //  companyService.addNewInvoice(invoice);
        return "redirect:/company/myinvoices";
    }

    private void addCompanyNameToModel(Model model) {
        Optional<Company> company = companyService.getCompanyNameByUsername(getAuthenticatedUsername());
        model.addAttribute("username", company.get().getName());
    }

    private void addCompanyToModel(Model model) {
        model.addAttribute("company", companyService.getCompanyByUsername(getAuthenticatedUsername()).get());
    }

    private void addServicesToModel(Model model) {
        model.addAttribute("services", serviceService.getServicesByCompanyName(companyService.getCompanyNameByUsername(authUtils.getAuthenticatedUsername()).get().getName()));
    }

    private void addUsernameToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }

    private void addCategoriesToModel(Model model) {
        model.addAttribute("categories", categoryService.getAll());
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
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
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setServiceService(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @Autowired
    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    @Autowired
    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Autowired
    public void setAuthUtils(AuthUtils authUtils) {
        this.authUtils = authUtils;
    }


}

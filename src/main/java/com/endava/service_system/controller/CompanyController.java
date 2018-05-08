package com.endava.service_system.controller;

import com.endava.service_system.dto.CompanyRegistrationDTO;
import com.endava.service_system.dto.NewInvoiceDTO;
import com.endava.service_system.dto.NewServiceDTO;
import com.endava.service_system.dto.UserDtoToShow;
import com.endava.service_system.model.*;
import com.endava.service_system.service.*;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;


@Controller
public class CompanyController {

    private static Logger LOGGER= LogManager.getLogger(CompanyController.class);
    private CompanyService companyService;
    private ConversionService conversionService;
    private CategoryService categoryService;
    private ServiceService serviceService;
    private ContractService contractService;
    private InvoiceService invoiceService;

    @GetMapping("/company/registration")
    public String getCompanyRegistrationForm(Model model) {
        model.addAttribute("company", new CompanyRegistrationDTO());
        return "companyRegistration";
    }

    @PostMapping("/company/registration")
    public String registerCompany(Model model, @ModelAttribute("company") @Valid CompanyRegistrationDTO companyRegistrationDTO, BindingResult bindingResult) {
        LOGGER.log(Level.DEBUG,companyRegistrationDTO);

        if (bindingResult.hasErrors()) {
            model.addAttribute("company", companyRegistrationDTO);
            return "companyRegistration";
        }
        companyService.save(conversionService.convert(companyRegistrationDTO, Company.class));
        return "redirect:/login";
    }

    @GetMapping("/company/profile")
    public String getCompanyProfile(Model model){
        addCompanyNameToModel(model);
        return "companyProfilePage";
    }

    @GetMapping(value = "/company/cabinet")
    public String userCabinet(Model model){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        UserDtoToShow user = conversionService.convert(userService.getByUsername(username).get(), UserDtoToShow.class);
//        model.addAttribute("username", username);
//        model.addAttribute("user", user);
        addCompanyToModel(model);
        return "userCabinet";
    }

    @GetMapping(value = "/company/serviceList")
    public String userServiceList(Model model){
        addCompanyNameToModel(model);
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        return  "companyServiceList";
    }

    @GetMapping(value = "/company/mycontracts")
    public String getMyContractsPage(Model model){
        addCompanyNameToModel(model);
        return  "companyContractList";
    }

    @GetMapping(value = "/company/notifications")
    public String getMyNotificationsPage(Model model){
        addCompanyNameToModel(model);
        return  "companyNotifications";
    }

    @GetMapping(value = "/company/myinvoices")
    public String getMyInvoicesPage(Model model){
        addCompanyNameToModel(model);
        return  "companyInvoices";
    }

    @GetMapping(value = "/company/notifications")
    public String getNotificationsPage(Model model){
        addCompanyNameToModel(model);
        return  "companyNotifications";
    }

    @GetMapping("/company/addservice")
    public String getCompanyAddServiceForm(Model model) {
        model.addAttribute("service", new NewServiceDTO());
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        return "companyAddService";
    }

    @PostMapping("/company/addservice")
    public String registerNewService(Model model, @ModelAttribute("service") @Valid NewServiceDTO newServiceDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("service", newServiceDTO);
            return "companyAddService";
        }
        Service service = serviceService.save(conversionService.convert(newServiceDTO, Service.class));
        companyService.addNewService(service);
        return "redirect:/company/serviceList";
    }

    @GetMapping("/contract/{id}/createInvoice")
    public String getCompanyCreateInvoiceForm(Model model, @PathVariable("id") Long contractId) {
        NewInvoiceDTO newInvoiceDTO = new NewInvoiceDTO();
        newInvoiceDTO.setContractId(contractId);
        model.addAttribute("invoice", newInvoiceDTO);

        //List<Contract> contracts = contractService.getAllContractsByCompanyUsername(getAuthenticatedUsername());
        //model.addAttribute("contracts", contracts);
        return "companyCreateInvoice";
    }

    @PostMapping("/company/createInvoice")
    public String registerNewService(Model model, @ModelAttribute("invoice") @Valid NewInvoiceDTO newInvoiceDTO, BindingResult bindingResult) {
        LOGGER.log(Level.DEBUG,newInvoiceDTO);
        LOGGER.log(Level.DEBUG,bindingResult.getAllErrors());
        if (bindingResult.hasErrors()) {
            model.addAttribute("invoice", newInvoiceDTO);
            return "companyCreateInvoice";
        }
        Contract contract = contractService.getContractById(newInvoiceDTO.getContractId());
        Invoice invoice = conversionService.convert(newInvoiceDTO, Invoice.class);
        invoice.setContract(contract);
        invoiceService.save(invoice);
      //  companyService.addNewInvoice(invoice);
        return "redirect:/company/serviceList";
    }

    private void addCompanyNameToModel(Model model){
        Optional<Company> company = companyService.getCompanyNameByUsername(getAuthenticatedUsername());
        model.addAttribute("username", company.get().getName());
    }

    private void addCompanyToModel(Model model){
        model.addAttribute("company", companyService.getCompanyByUsername(getAuthenticatedUsername()).get());
    }

    private String getAuthenticatedUsername(){
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

}

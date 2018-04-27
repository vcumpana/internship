package com.endava.service_system.controller;

import com.endava.service_system.dto.CompanyRegistrationDTO;
import com.endava.service_system.dto.UserDtoToShow;
import com.endava.service_system.model.Company;
import com.endava.service_system.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;


@Controller
public class CompanyController {

    private  CompanyService companyService;
    private  ConversionService conversionService;

    @GetMapping("/company/registration")
    public String getCompanyRegistrationForm(Model model) {
        model.addAttribute("company", new CompanyRegistrationDTO());
        return "companyRegistration";
    }

    @PostMapping("/company/registration")
    public String registerCompany(Model model, @ModelAttribute("company") @Valid CompanyRegistrationDTO companyRegistrationDTO, BindingResult bindingResult) {
        System.out.println(companyRegistrationDTO);

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
        return "userCabinet";
    }

    @GetMapping(value = "/company/serviceList")
    public String userServiceList(Model model){
        addCompanyNameToModel(model);
        return  "companyServiceList";
    }

    private void addCompanyNameToModel(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Company> company = companyService.getCompanyNameByUsername(authentication.getName());
        model.addAttribute("username", company.get().getName());
    }

    private void addUsernameToModel(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}

package com.endava.service_system.controller.rest;

import com.endava.service_system.model.dto.ServiceToCompanyDto;
import com.endava.service_system.model.dto.ServiceToUserDto;
import com.endava.service_system.model.entities.Category;
import com.endava.service_system.model.entities.Company;
import com.endava.service_system.model.entities.Service;
import com.endava.service_system.model.filters.ServiceDtoFilter;
import com.endava.service_system.model.filters.order.ServiceOrderBy;
import com.endava.service_system.service.CategoryService;
import com.endava.service_system.service.CompanyService;
import com.endava.service_system.service.ServiceService;
import com.endava.service_system.utils.AuthUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ServiceRestController {


    private final ServiceService serviceService;
    private final CompanyService companyService;
    private final CategoryService categoryService;
    private final AuthUtils authUtils;
    private static final Logger LOGGER = LogManager.getLogger(InvoicesRestController.class);


    public ServiceRestController(ServiceService serviceService, CompanyService companyService, CategoryService categoryService, AuthUtils authUtils) {
        this.serviceService = serviceService;
        this.companyService = companyService;
        this.categoryService = categoryService;
        this.authUtils = authUtils;
    }

    @PostMapping("service")
    @PreAuthorize("hasAnyRole('ROLE_COMPANY')")
    public ResponseEntity addService(@RequestBody Service service) {
        if (serviceService.saveService(service) == null)
            return new ResponseEntity(HttpStatus.CONFLICT);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("service")
    @PreAuthorize("hasAnyRole('ROLE_COMPANY')")
    public ResponseEntity updateService(@RequestBody Service service) {
        LOGGER.log(Level.DEBUG, service);
        if (serviceService.updateService(service) == null)
            return new ResponseEntity(HttpStatus.CONFLICT);
        return new ResponseEntity(HttpStatus.CREATED);
    }


    @DeleteMapping("service/{id}")
    @PreAuthorize("hasAnyRole('ROLE_COMPANY')")
    public ResponseEntity deleteService(@PathVariable("id") int id) {
        LOGGER.log(Level.DEBUG, "Delete service with id: "+ id);

        if (serviceService.deleteService(id) == 0)
            return new ResponseEntity("There is no such service", HttpStatus.CONFLICT);
        return new ResponseEntity("Service has been successfully deleted", HttpStatus.OK);
    }

    @GetMapping("/services/{id}")
    public ResponseEntity getAllServices(@PathVariable("id") int id) {
        ServiceToUserDto service = serviceService.getServiceToUserDtoById(id);
        if (service == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity(service, HttpStatus.OK);
    }

//    @GetMapping("/service/{id}/edit")
//    public ModelAndView editInvoice(@PathVariable("id") Long serviceId, HttpServletRequest request, Model model) {
//        Service service = serviceService.getServiceById(serviceId).get();
//        ModelAndView modelAndView = new ModelAndView();
//        Optional<Company> company = companyService.getCompanyNameByUsername(authUtils.getAuthenticatedUsername());
//        if (service != null) {
//            if (serviceService.getServicesByCompanyName(company.get().getName()).contains(service)) {
//                List<Category> categories = categoryService.getAll();
//                modelAndView.setViewName("companyEditService");
//                modelAndView.addObject("categories", categories);
//                modelAndView.addObject("username", company.get().getName());
//                modelAndView.addObject("service", service);
//                return modelAndView;
//            }
//        }
//        modelAndView.setViewName("redirect:/company/serviceList");
//        return modelAndView;
//    }

//    @PutMapping("service")
//    public ResponseEntity updateService(@RequestBody Service service){
//        if (serviceService.updateService(service) == null)
//            return new ResponseEntity(HttpStatus.CONFLICT);
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @GetMapping("/services")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Map<String, Object> getServices(@RequestParam(value = "categoryId", required = false) Long categoryId,
                                           @RequestParam(value = "size", required = false) Integer size,
                                           @RequestParam(value = "page", required = false) Integer page,
                                           @RequestParam(required = false, value = "min") Integer min,
                                           @RequestParam(required = false, value = "companyId") Long companyId,
                                           @RequestParam(required = false, value = "company") String companyName,
                                           @RequestParam(required = false, value = "category") String categoryName,
                                           @RequestParam(required = false, value = "max") Integer max,
                                           @RequestParam(required = false, value = "order") String order,
                                           @RequestParam(required = false,value = "orderBy")ServiceOrderBy orderBy ) {
        Map<String, Object> map = new HashMap<>();
        Sort.Direction direction = getDirection(order);
        ServiceDtoFilter filter = ServiceDtoFilter.builder()
                .size(size)
                .order(direction)
                .min(min)
                .categoryId(categoryId)
                .companyId(companyId)
                .categoryName(categoryName)
                .companyName(companyName)
                .max(max)
                .orderBy(orderBy)
                .page(page)
                .build();
        map.put("services", serviceService.getServicesWithFilter(filter));
        map.put("pages", serviceService.getPagesSize(filter));
        return map;
    }

    @GetMapping("/{companyName}/services")
    public ResponseEntity getServicesByCompanyName(@PathVariable("companyName") String companyName) {
        List<ServiceToCompanyDto> services = serviceService.getServicesDtoByCompanyName(companyName);
        if (services != null)
            return new ResponseEntity(services, HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/services/getPDF", produces = "application/pdf")
    public byte[] getPDFOfServices() {
        ByteArrayOutputStream stream = serviceService.getPdfOfServices();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return stream.toByteArray();
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

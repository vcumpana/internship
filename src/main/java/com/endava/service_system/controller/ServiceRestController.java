package com.endava.service_system.controller;

import com.endava.service_system.dto.ServiceToUserDto;
import com.endava.service_system.model.Company;
import com.endava.service_system.model.Service;
import com.endava.service_system.service.ServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ServiceRestController {


    private final ServiceService serviceService;

    public ServiceRestController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @PostMapping("service")
    public ResponseEntity addService(@RequestBody Service service){
        if (serviceService.saveService(service) == null)
            return new ResponseEntity(HttpStatus.CONFLICT);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("service/{id}")
    public ResponseEntity deleteService(@PathVariable("id") int id){
        if (!serviceService.deleteService(id).isPresent())
            return new ResponseEntity(HttpStatus.CONFLICT);
        return new ResponseEntity(HttpStatus.OK);
    }

//    @PutMapping("service")
//    public ResponseEntity updateService(@RequestBody Service service){
//        if (serviceService.updateService(service) == null)
//            return new ResponseEntity(HttpStatus.CONFLICT);
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @GetMapping("/services")
    public ResponseEntity getAllServices(Model model, @RequestParam(required = false, value = "category") String categoryName){
        List<ServiceToUserDto> services = null;
        if (categoryName == null)
            services = serviceService.getAllServices();
        else
            services = serviceService.getServicesByCategoryName(categoryName);
        return new ResponseEntity( services, HttpStatus.OK);
    }

    @GetMapping("/{companyName}/services")
    public ResponseEntity getServicesByCompanyName(@PathVariable("companyName") String companyName){
        List<Service> services = serviceService.getServicesByCompanyName(companyName);
        if (services != null)
            return new ResponseEntity(services, HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}

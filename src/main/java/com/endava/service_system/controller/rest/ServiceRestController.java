package com.endava.service_system.controller.rest;

import com.endava.service_system.dto.ServiceToUserDto;
import com.endava.service_system.model.Company;
import com.endava.service_system.model.Service;
import com.endava.service_system.model.ServiceDtoFilter;
import com.endava.service_system.service.ServiceService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public ResponseEntity addService(@RequestBody Service service) {
        if (serviceService.saveService(service) == null)
            return new ResponseEntity(HttpStatus.CONFLICT);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("service/{id}")
    public ResponseEntity deleteService(@PathVariable("id") int id) {
        if (!serviceService.deleteService(id).isPresent())
            return new ResponseEntity(HttpStatus.CONFLICT);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/services/{id}")
    public ResponseEntity getAllServices(@PathVariable("id") int id){
        ServiceToUserDto service = serviceService.getServiceToUserDtoById(id);
        if (service == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity( service, HttpStatus.OK);
    }

//    @PutMapping("service")
//    public ResponseEntity updateService(@RequestBody Service service){
//        if (serviceService.updateService(service) == null)
//            return new ResponseEntity(HttpStatus.CONFLICT);
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @GetMapping("/services")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Map<String,Object> getServices(@RequestParam(value = "categoryId", required = false) Long categoryId,
                                              @RequestParam(value = "size", required = false) Integer size,
                                              @RequestParam(value = "page", required = false) Integer page,
                                              @RequestParam(required = false, value = "min") Integer min,
                                              @RequestParam(required = false, value = "companyId") Long companyId,
                                              @RequestParam(required = false, value = "company") String companyName,
                                              @RequestParam(required = false, value = "category") String categoryName,
                                              @RequestParam(required = false, value = "max") Integer max,
                                              @RequestParam(required = false, value = "orderByPrice") String order) {
        Map<String,Object> map=new HashMap<>();
        Sort.Direction direction = getDirection(order);
        ServiceDtoFilter filter = ServiceDtoFilter.builder()
                .size(size)
                .direction(direction)
                .min(min)
                .categoryId(categoryId)
                .companyId(companyId)
                .categoryName(categoryName)
                .companyName(companyName)
                .max(max)
                .page(page)
                .build();
        map.put("services",serviceService.getServicesWithFilter(filter));
        map.put("pages",serviceService.getPagesSize(filter));
        return map;
    }

    @GetMapping("/{companyName}/services")
    public ResponseEntity getServicesByCompanyName(@PathVariable("companyName") String companyName) {
        List<Service> services = serviceService.getServicesByCompanyName(companyName);
        if (services != null)
            return new ResponseEntity(services, HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NOT_FOUND);
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

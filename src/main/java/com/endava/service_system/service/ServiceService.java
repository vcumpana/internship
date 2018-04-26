package com.endava.service_system.service;

import com.endava.service_system.dao.ServiceDao;
import com.endava.service_system.dto.ServiceToUserDto;
import com.endava.service_system.model.Service;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ServiceService {

    private ServiceDao serviceDao;
    private CompanyService companyService;
    private final ConversionService conversionService;


    public ServiceService(ServiceDao serviceDao, CompanyService companyService, ConversionService conversionService) {
        this.serviceDao = serviceDao;
        this.companyService = companyService;
        this.conversionService = conversionService;
    }

    public Service saveService(Service service) {
        return serviceDao.save(service);
    }

    public List<ServiceToUserDto> getAllServices() {
        List<ServiceToUserDto> services = new ArrayList<>();
        List<Map> maps = serviceDao.getAll();
        for (Map map: maps)
            services.add( conversionService.convert(map, ServiceToUserDto.class));
        return services;
    }

    public List<Service> getServicesByCompanyName(String companyName) {
        return companyService.getCompanyByNameWithServices(companyName)
                .get()
                .getServices().stream()
                .collect(Collectors.toList());
    }

    public List<ServiceToUserDto> getServicesByCategoryName(String categoryName) {
        List<ServiceToUserDto> services = new ArrayList<>();
        List<Map> maps = serviceDao.getByCategoryName(categoryName);
        for (Map map: maps)
            services.add( conversionService.convert(map, ServiceToUserDto.class));
        return services;
    }

//    public boolean updateService(Service service) {
//       return serviceDao.updateService(service);
//    }

    public Optional<Service> deleteService(int id) {
        return serviceDao.deleteServicesById(id);
    }
}

package com.endava.service_system.service;

import com.endava.service_system.dao.ServiceDao;
import com.endava.service_system.dao.ServiceToUserDao;
import com.endava.service_system.dto.ServiceToUserDto;
import com.endava.service_system.model.Service;
import com.endava.service_system.model.ServiceDtoFilter;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ConversionService conversionService;
    private ServiceToUserDao serviceToUserDao;

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
        ServiceDtoFilter dtoFilter=new ServiceDtoFilter();
        dtoFilter.setCategoryName(categoryName);
        return serviceToUserDao.getAllServices(dtoFilter);
    }

    public List<ServiceToUserDto> getServicesWithFilter(ServiceDtoFilter dtoFilter){
        return serviceToUserDao.getAllServices(dtoFilter);
    }

    public Optional<Service> deleteService(int id) {
        return serviceDao.deleteServicesById(id);
    }

    public Optional<Service> getServiceById(int serviceId) {
        return serviceDao.getById(serviceId);
    }

    public ServiceToUserDto getServiceToUserDtoById(int id) {
        List<ServiceToUserDto> services = new ArrayList<>();
        List<Map> maps = serviceDao.getServiceDtoById(id);
        for (Map map: maps)
            services.add( conversionService.convert(map, ServiceToUserDto.class));
        if (services.size() == 1)
            return services.get(0);
        else
            return null;
    }

    @Autowired
    public void setServiceDao(ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
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
    public void setServiceToUserDao(ServiceToUserDao serviceToUserDao) {
        this.serviceToUserDao = serviceToUserDao;
    }
}

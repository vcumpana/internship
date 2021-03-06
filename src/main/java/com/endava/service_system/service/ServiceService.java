package com.endava.service_system.service;

import com.endava.service_system.dao.ServiceDao;
import com.endava.service_system.dao.ServiceToUserDao;
import com.endava.service_system.model.dto.ServiceToCompanyDto;
import com.endava.service_system.model.dto.ServiceToUserDto;
import com.endava.service_system.model.entities.Service;
import com.endava.service_system.utils.PDFMaking;
import org.springframework.beans.factory.annotation.Autowired;
import com.endava.service_system.model.filters.ServiceDtoFilter;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
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

    public Service updateService(Service service) {
      //  companyService.updateService(service);
        return serviceDao.save(service);
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

    public int deleteService(long id) {
        companyService.deleteServiceByIdFromCompany(id);
        return serviceDao.deleteServicesById(id);
    }

    public Optional<Service> getServiceById(long serviceId) {
        return serviceDao.getById(serviceId);
    }

    public ServiceToUserDto getServiceToUserDtoById(long id) {
        List<ServiceToUserDto> services = new ArrayList<>();
        List<Map> maps = serviceDao.getServiceDtoById(id);
        for (Map map: maps)
            services.add( conversionService.convert(map, ServiceToUserDto.class));
        if (services.size() == 1)
            return services.get(0);
        else
            return null;
    }

    public Service save(Service service) {
        return serviceDao.save(service);
    }

    public ByteArrayOutputStream getPdfOfServices(){
        return PDFMaking.makePDFOfServices(getAllServices());
    }

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Autowired
    public void setServiceToUserDao(ServiceToUserDao serviceToUserDao) {
        this.serviceToUserDao = serviceToUserDao;
    }

    @Autowired
    public void setServiceDao(ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
    }

    @Autowired
    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    public Long getPagesSize(ServiceDtoFilter filter) {
        return serviceToUserDao.getPagesSize(filter);
    }

    public Optional<Service> getServicesByTitle(String title) {
        return serviceDao.getByTitle(title);
    }

    public List<ServiceToCompanyDto> getServicesDtoByCompanyName(String companyName) {
        List<Service> services = getServicesByCompanyName(companyName);
        List<ServiceToCompanyDto> servicesDto = new ArrayList<>();
        for(Service service : services){
            ServiceToCompanyDto serviceDto = conversionService.convert(service, ServiceToCompanyDto.class);
            serviceDto.setNumberOfContracts(serviceDao.countContracts(serviceDto.getTitle(), companyName));
            servicesDto.add(serviceDto);
        }
        return servicesDto;
    }

    public List<Service> getServicesByTitleAndId(String title, long id) {
        return serviceDao.getByTitleAndIdIsNot(title, id);
    }
}

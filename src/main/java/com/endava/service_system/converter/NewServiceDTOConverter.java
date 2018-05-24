package com.endava.service_system.converter;

import com.endava.service_system.model.dto.NewServiceDTO;
import com.endava.service_system.model.dto.ServiceToUserDto;
import com.endava.service_system.model.entities.Service;
import com.endava.service_system.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class NewServiceDTOConverter implements Converter<NewServiceDTO, Service> {

    private CategoryService categoryService;

    @Override
    public Service convert(NewServiceDTO newServiceDTO) {
        Service service = new Service();
        service.setCategory(categoryService.getByName(newServiceDTO.getCategory()).get());
        service.setDescription(newServiceDTO.getDescription());
        service.setTitle(newServiceDTO.getTitle());
        service.setPrice(newServiceDTO.getPrice());
        return service;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}

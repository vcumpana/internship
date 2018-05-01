package com.endava.service_system.utils;

import com.endava.service_system.dto.NewServiceDTO;
import com.endava.service_system.dto.ServiceToUserDto;
import com.endava.service_system.model.Service;
import com.endava.service_system.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

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

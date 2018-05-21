package com.endava.service_system.constraints.validator;

import com.endava.service_system.constraints.ServiceTitleInUseConstraint;
import com.endava.service_system.model.dto.NewServiceDTO;
import com.endava.service_system.model.entities.Service;
import com.endava.service_system.service.ServiceService;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ServiceTitleExistsConstraintValidator implements ConstraintValidator<ServiceTitleInUseConstraint, NewServiceDTO> {

    private ServiceService serviceService;
    private String title;
    private String id;

    public ServiceTitleExistsConstraintValidator(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    public void initialize(ServiceTitleInUseConstraint constraint) {
        this.id = constraint.id();
        this.title = constraint.fieldName();
    }

    public boolean isValid(NewServiceDTO newServiceDTO, ConstraintValidatorContext context) {
        String titl = (String) new BeanWrapperImpl(newServiceDTO)
                .getPropertyValue(title);
        long id1 = (long) new BeanWrapperImpl(newServiceDTO)
                .getPropertyValue(id);
        //TODO if id==0 check if it is unique;


        //TODO if id!=0 then check if its unique (except himself)
        List<Service> service = serviceService.getServicesByTitleAndId(titl, id1);
        if (service.isEmpty()) {
            return true;
        }

        return false;
    }
}

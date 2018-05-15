package com.endava.service_system.constraints;

import com.endava.service_system.model.Company;
import com.endava.service_system.model.Service;
import com.endava.service_system.service.CompanyService;
import com.endava.service_system.service.ServiceService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class ServiceTitleExistsConstraintValidator implements ConstraintValidator<ServiceTitleInUseConstraint, String> {

    private ServiceService serviceService;

    public ServiceTitleExistsConstraintValidator(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    public void initialize(ServiceTitleInUseConstraint constraint) {
    }

    public boolean isValid(String title, ConstraintValidatorContext context) {
        Optional<Service> service = serviceService.getServicesByTitle(title);
        if (service.isPresent()) {
            return false;
        }
        return true;
    }
}

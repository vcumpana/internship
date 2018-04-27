package com.endava.service_system.constraints;

import com.endava.service_system.model.Company;
import com.endava.service_system.service.CompanyService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class EmailExistsConstraintValidator implements ConstraintValidator<EmailInUseConstraint, String> {

    private CompanyService companyService;

    public EmailExistsConstraintValidator(CompanyService companyService) {
        this.companyService = companyService;
    }

    public void initialize(EmailInUseConstraint constraint) {
    }

    public boolean isValid(String email, ConstraintValidatorContext context) {
        Optional<Company> company = companyService.getCompanyByEmail(email);
        if (company.isPresent()) {
            return false;
        }
        return true;
    }
}

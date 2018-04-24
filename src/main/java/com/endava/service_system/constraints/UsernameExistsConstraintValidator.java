package com.endava.service_system.constraints;

import com.endava.service_system.model.Company;
import com.endava.service_system.service.CompanyService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UsernameExistsConstraintValidator implements ConstraintValidator<UsernameInUseConstraint, String> {

   private CompanyService companyService;

   public UsernameExistsConstraintValidator(CompanyService companyService) {
      this.companyService = companyService;
   }


   public void initialize(UsernameInUseConstraint constraint) {
   }

   public boolean isValid(String username, ConstraintValidatorContext context) {
       Optional<Company> company = companyService.getCompanyByUsername(username);
       if (company.isPresent()) {
           return false;
       }
       return true;
   }
}

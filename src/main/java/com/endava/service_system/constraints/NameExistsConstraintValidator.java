package com.endava.service_system.constraints;

import com.endava.service_system.model.Company;
import com.endava.service_system.service.CompanyService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class NameExistsConstraintValidator implements ConstraintValidator<NameInUseConstraint, String> {

   private CompanyService companyService;

   public NameExistsConstraintValidator(CompanyService companyService) {
      this.companyService = companyService;
   }

   public void initialize(NameInUseConstraint constraint) {
   }

   public boolean isValid(String name, ConstraintValidatorContext context){
      Optional<Company> company = companyService.getCompanyByName(name);
       if (company.isPresent())
      return false;
       return true;
   }
}

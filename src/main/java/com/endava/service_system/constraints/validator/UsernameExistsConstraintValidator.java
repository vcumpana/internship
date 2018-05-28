package com.endava.service_system.constraints.validator;

import com.endava.service_system.constraints.UsernameInUseConstraint;
import com.endava.service_system.model.entities.Credential;
import com.endava.service_system.service.CredentialService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UsernameExistsConstraintValidator implements ConstraintValidator<UsernameInUseConstraint, String> {

   private CredentialService credentialService;

    public UsernameExistsConstraintValidator(CredentialService credentialService) {
        this.credentialService = credentialService;
    }


   public void initialize(UsernameInUseConstraint constraint) {

   }

   public boolean isValid(String username, ConstraintValidatorContext context) {
       Optional<Credential> credential = credentialService.getByUsername(username);
       if (credential.isPresent()) {
           return false;
       }
       return true;
   }
}

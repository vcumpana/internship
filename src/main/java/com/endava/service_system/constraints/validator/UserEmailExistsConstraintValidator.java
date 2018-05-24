package com.endava.service_system.constraints.validator;

import com.endava.service_system.constraints.UserEmailInUseConstraint;
import com.endava.service_system.model.entities.User;
import com.endava.service_system.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UserEmailExistsConstraintValidator implements ConstraintValidator<UserEmailInUseConstraint, String> {
    private UserService userService;

    public UserEmailExistsConstraintValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> user = userService.getByEmail(email);
        if(user.isPresent()){
            return false;
        }
        return true;
    }

    @Override
    public void initialize(UserEmailInUseConstraint constraintAnnotation) {

    }
}

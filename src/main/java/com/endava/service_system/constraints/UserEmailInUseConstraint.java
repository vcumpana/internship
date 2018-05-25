package com.endava.service_system.constraints;

import com.endava.service_system.constraints.validator.UserEmailExistsConstraintValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UserEmailExistsConstraintValidator.class)
public @interface UserEmailInUseConstraint {
    String message() default "Email already occupied";
    String fieldName() default "";
    Class[] groups() default {};
    Class[] payload() default {};
}

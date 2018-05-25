package com.endava.service_system.constraints;

import com.endava.service_system.constraints.validator.UsernameExistsConstraintValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UsernameExistsConstraintValidator.class)
public @interface UsernameInUseConstraint {
    String message() default "Username already exists";

    String fieldName() default "";

    Class[] groups() default {};

    Class[] payload() default {};
}


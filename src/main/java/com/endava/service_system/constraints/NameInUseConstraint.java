package com.endava.service_system.constraints;

import com.endava.service_system.constraints.validator.NameExistsConstraintValidator;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NameExistsConstraintValidator.class)
public @interface NameInUseConstraint {
    String message() default "Company with this name already exists";

    String fieldName() default "";

    Class[] groups() default {};

    Class[] payload() default {};
}


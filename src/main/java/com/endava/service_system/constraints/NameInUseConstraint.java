package com.endava.service_system.constraints;

import javax.validation.Constraint;
import java.lang.annotation.*;

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


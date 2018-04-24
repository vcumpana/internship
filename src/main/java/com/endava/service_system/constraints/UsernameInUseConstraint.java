package com.endava.service_system.constraints;

import javax.validation.Constraint;
import java.lang.annotation.*;

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


package com.endava.service_system.constraints;


import org.springframework.context.annotation.Configuration;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EmailExistsConstraintValidator.class)
public @interface EmailInUseConstraint {
    String message() default "Email already occupied";
    String fieldName() default "";
    Class[] groups() default {};
    Class[] payload() default {};
}

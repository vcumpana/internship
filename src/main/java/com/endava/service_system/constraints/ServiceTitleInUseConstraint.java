package com.endava.service_system.constraints;


import com.endava.service_system.constraints.validator.ServiceTitleExistsConstraintValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ServiceTitleExistsConstraintValidator.class)
public @interface ServiceTitleInUseConstraint {
    String message() default "Please choose another title for your service. This title already exists";
    String fieldName() default "";
    Class[] groups() default {};
    Class[] payload() default {};
}

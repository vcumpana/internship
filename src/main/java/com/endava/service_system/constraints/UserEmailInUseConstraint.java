package com.endava.service_system.constraints;

import javax.validation.Constraint;
import java.lang.annotation.*;

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

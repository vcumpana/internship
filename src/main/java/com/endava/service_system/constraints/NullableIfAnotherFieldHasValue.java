package com.endava.service_system.constraints;

import com.endava.service_system.constraints.validator.NullableIfAnotherFieldHasValueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = NullableIfAnotherFieldHasValueValidator.class)
@Documented
public @interface NullableIfAnotherFieldHasValue {
    String firstField();
    String secondField();
    String message() default "You need to provide at least one parameter";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

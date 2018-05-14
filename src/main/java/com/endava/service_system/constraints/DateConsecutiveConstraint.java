package com.endava.service_system.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.LocalDate;

@Constraint(validatedBy = DateConsecutiveValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DateConsecutiveConstraint {

    String message() default "";

    String beginDate();
    String endDate();

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        DateConsecutiveConstraint[] value();
    }
}

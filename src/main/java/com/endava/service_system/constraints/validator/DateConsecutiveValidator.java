package com.endava.service_system.constraints.validator;


import com.endava.service_system.constraints.DateConsecutiveConstraint;
import com.endava.service_system.model.dto.NewInvoiceDTO;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateConsecutiveValidator implements ConstraintValidator<DateConsecutiveConstraint, NewInvoiceDTO> {

    private String beginDate;
    private String endDate;

    @Override
    public void initialize(DateConsecutiveConstraint constraintAnnotation) {
        this.beginDate = constraintAnnotation.beginDate();
        this.endDate = constraintAnnotation.endDate();
    }

    @Override
    public boolean isValid(NewInvoiceDTO newInvoiceDTO, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate firstDate = (LocalDate) new BeanWrapperImpl(newInvoiceDTO)
                .getPropertyValue(beginDate);
        LocalDate secondDate = (LocalDate) new BeanWrapperImpl(newInvoiceDTO)
                .getPropertyValue(endDate);

        if (firstDate == null || secondDate == null)
            return false;
        return !firstDate.isAfter(secondDate);
    }
}

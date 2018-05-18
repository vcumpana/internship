package com.endava.service_system.constraints.validator;

import com.endava.service_system.constraints.NullableIfAnotherFieldHasValue;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullableIfAnotherFieldHasValueValidator implements ConstraintValidator<NullableIfAnotherFieldHasValue,Object> {
    private String firstField;
    private String secondField;
    @Override
    public void initialize(NullableIfAnotherFieldHasValue constraintAnnotation) {
        firstField= constraintAnnotation.firstField();
        secondField= constraintAnnotation.secondField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object firstFieldValue= new BeanWrapperImpl(value)
                .getPropertyValue(firstField);
        Object secondFieldValue= new BeanWrapperImpl(value)
                .getPropertyValue(secondField);
        boolean firstFieldNull=firstFieldValue==null;
        boolean secondFieldNull=secondFieldValue==null;
        if(!firstFieldNull|| !secondFieldNull){
            return true;
        }
        return false;
    }
}

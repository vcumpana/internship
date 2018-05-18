package com.endava.service_system.constraints.validator;

import com.endava.service_system.constraints.FieldsValueMatch;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldsValueMatchValidator implements ConstraintValidator<FieldsValueMatch, Object> {
   private String field;
   private String fieldMatch;

   public void initialize(FieldsValueMatch constraintAnnotation) {
      this.field = constraintAnnotation.field();
      this.fieldMatch = constraintAnnotation.fieldMatch();
   }

   @Override
   public boolean isValid(Object s, ConstraintValidatorContext constraintValidatorContext) {
      Object fieldValue = new BeanWrapperImpl(s)
              .getPropertyValue(field);
      Object fieldMatchValue = new BeanWrapperImpl(s)
              .getPropertyValue(fieldMatch);

      if (fieldValue != null) {
         return fieldValue.equals(fieldMatchValue);
      } else {
         return fieldMatchValue == null;
      }
   }
}

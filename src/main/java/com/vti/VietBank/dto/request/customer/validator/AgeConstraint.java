package com.vti.VietBank.dto.request.customer.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = AgeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeConstraint {
    String message() default "CUSTOMER_DOB_INVALID";
    
    int min() default 18;
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}

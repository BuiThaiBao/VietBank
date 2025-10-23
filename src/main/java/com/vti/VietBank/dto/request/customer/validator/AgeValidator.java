package com.vti.VietBank.dto.request.customer.validator;

import java.time.LocalDate;
import java.time.Period;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AgeValidator implements ConstraintValidator<AgeConstraint, LocalDate> {
    
    private int minAge;
    
    @Override
    public void initialize(AgeConstraint constraintAnnotation) {
        this.minAge = constraintAnnotation.min();
    }
    
    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext context) {
        if (dateOfBirth == null) {
            return true; // @NotNull sẽ xử lý trường hợp null
        }
        
        LocalDate today = LocalDate.now();
        int age = Period.between(dateOfBirth, today).getYears();
        
        return age >= minAge;
    }
}

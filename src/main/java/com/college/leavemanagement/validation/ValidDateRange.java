package com.college.leavemanagement.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "Return/To date must be on or after From date, and From date must be today or in the future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

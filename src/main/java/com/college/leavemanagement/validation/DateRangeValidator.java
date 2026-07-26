package com.college.leavemanagement.validation;

import com.college.leavemanagement.dto.leave.LeaveSubmitRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, LeaveSubmitRequest> {

    @Override
    public boolean isValid(LeaveSubmitRequest request, ConstraintValidatorContext context) {
        if (request == null || request.getFromDate() == null || request.getToDate() == null) {
            return true; // Let @NotNull handle null checks
        }

        LocalDate today = LocalDate.now();
        if (request.getFromDate().isBefore(today)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("From date must be today or in the future")
                    .addPropertyNode("fromDate")
                    .addConstraintViolation();
            return false;
        }

        if (request.getToDate().isBefore(request.getFromDate())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("To/Return date must be on or after From date")
                    .addPropertyNode("toDate")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}

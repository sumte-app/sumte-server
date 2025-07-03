package com.sumte.apiPayload.exception.validator;

import com.sumte.apiPayload.code.error.CommonErrorCode;
import com.sumte.apiPayload.exception.annotation.CheckPageSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CheckPageSizeValidator implements ConstraintValidator<CheckPageSize, Integer> {
    @Override
    public void initialize(CheckPageSize constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer pageSize, ConstraintValidatorContext context) {
        boolean isValid = pageSize >= 1;

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(CommonErrorCode.PAGE_SIZE_UNDER_ONE.getMessage())
                    .addConstraintViolation();
        }
        return isValid;
    }
}

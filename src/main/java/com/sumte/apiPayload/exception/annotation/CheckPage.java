package com.sumte.apiPayload.exception.annotation;

import com.sumte.apiPayload.exception.validator.CheckPageValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckPageValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPage {

    String message() default "페이지는 1이상 부터 입력이 가능합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

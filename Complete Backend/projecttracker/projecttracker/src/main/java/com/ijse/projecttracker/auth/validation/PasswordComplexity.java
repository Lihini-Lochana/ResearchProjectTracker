package com.ijse.projecttracker.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordComplexityValidator.class)
public @interface PasswordComplexity {
    String message() default "password must be at least {min} chars, contain a digit and a special character";
    int min() default 8;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

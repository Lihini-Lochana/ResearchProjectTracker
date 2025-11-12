package com.ijse.projecttracker.auth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
public @interface PasswordMatches {
    String message() default "password and confirmPassword do not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

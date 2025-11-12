package com.ijse.projecttracker.auth.validation;

import com.ijse.projecttracker.auth.dto.SignupRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, SignupRequest> {
    @Override
    public boolean isValid(SignupRequest req, ConstraintValidatorContext ctx) {
        if (req == null) return true;
        return req.getPassword() != null && req.getPassword().equals(req.getConfirmPassword());
    }
}

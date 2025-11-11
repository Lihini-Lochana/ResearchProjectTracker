package com.ijse.projecttracker.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordComplexityValidator implements ConstraintValidator<PasswordComplexity, String> {
    private int min;
    @Override
    public void initialize(PasswordComplexity ann) { this.min = ann.min(); }
    @Override
    public boolean isValid(String pw, ConstraintValidatorContext ctx) {
        if (pw == null) return false;
        if (pw.length() < min) return false;
        boolean hasDigit = pw.chars().anyMatch(Character::isDigit);
        String specials = "!@#$%^&*()_+[]{}|;:'\",.<>/?`~-=";
        boolean hasSpecial = pw.chars().anyMatch(c -> specials.indexOf(c) >= 0);
        return hasDigit && hasSpecial;
    }
}

package com.example.ecommerce.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validates the {@link ValidPassword} constraint.
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,64}$");

    /**
     * Validates the password complexity rules.
     *
     * @param value   the candidate password
     * @param context the validation context
     * @return {@code true} when valid (or blank, handled by {@code @NotBlank})
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return PASSWORD_PATTERN.matcher(value).matches();
    }
}

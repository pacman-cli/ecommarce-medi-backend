package com.example.ecommerce.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Jakarta Validation constraint enforcing password complexity.
 *
 * <p>Requires 8-64 characters containing at least one lowercase letter, one
 * uppercase letter, one digit and one special character.</p>
 */
@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    /** Default validation message. */
    String message() default "Password must be 8-64 characters and contain an uppercase letter, "
            + "a lowercase letter, a digit and a special character";

    /** Standard validation groups. */
    Class<?>[] groups() default {};

    /** Standard payload carriers. */
    Class<? extends Payload>[] payload() default {};
}

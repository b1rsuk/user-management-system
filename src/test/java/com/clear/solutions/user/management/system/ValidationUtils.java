package com.clear.solutions.user.management.system;

import jakarta.validation.ConstraintViolationException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtils {

    public static String extractConstraintViolationsMessage(ConstraintViolationException constraintViolationException) {
        return constraintViolationException
                .getConstraintViolations()
                .iterator()
                .next()
                .getMessage();
    }

}

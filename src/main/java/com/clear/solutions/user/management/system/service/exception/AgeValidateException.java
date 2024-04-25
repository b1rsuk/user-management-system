package com.clear.solutions.user.management.system.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AgeValidateException extends RuntimeException {

    public AgeValidateException(int minimumAllowedAge) {
        super(String.format("You must be at least %d years old.", minimumAllowedAge));
    }

}

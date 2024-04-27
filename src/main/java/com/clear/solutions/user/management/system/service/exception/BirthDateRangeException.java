package com.clear.solutions.user.management.system.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BirthDateRangeException extends RuntimeException {

    public BirthDateRangeException(LocalDate from, LocalDate to) {
        super(String.format("Invalid birth date range: Start date '%s' must be before end date '%s'.", from, to));
    }

}
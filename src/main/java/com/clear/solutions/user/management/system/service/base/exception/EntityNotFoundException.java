package com.clear.solutions.user.management.system.service.base.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Long id) {
        super(String.format("Entity with ID %d was not found", id));
    }

}

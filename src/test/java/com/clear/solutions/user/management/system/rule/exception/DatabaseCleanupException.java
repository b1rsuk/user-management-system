package com.clear.solutions.user.management.system.rule.exception;

public class DatabaseCleanupException extends RuntimeException {
    public DatabaseCleanupException(Exception exception) {
        super(exception);
    }
}

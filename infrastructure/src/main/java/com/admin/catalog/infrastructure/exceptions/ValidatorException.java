package com.admin.catalog.infrastructure.exceptions;

public class ValidatorException extends Exception {

    public ValidatorException(final String errorMessage, final String objectType) {
        super(objectType + ": " + errorMessage);
    }

}

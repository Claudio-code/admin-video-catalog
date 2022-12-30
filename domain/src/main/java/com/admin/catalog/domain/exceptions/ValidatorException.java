package com.admin.catalog.domain.exceptions;

public class ValidatorException extends Exception {

    public ValidatorException(final String errorMessage, final String objectType) {
        super(objectType + ": " + errorMessage);
    }

}

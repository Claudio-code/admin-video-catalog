package com.admin.catalog.domain.validation;

public abstract class Validator {

    private final ValidationHandler handler;

    protected Validator(final ValidationHandler aHandler) {
        handler = aHandler;
    }

    protected ValidationHandler validationHandler() {
        return handler;
    }

    public abstract void validate();

}

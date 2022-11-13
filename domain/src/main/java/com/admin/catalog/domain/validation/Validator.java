package com.admin.catalog.domain.validation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Validator {

    private final ValidationHandler handler;

    protected ValidationHandler validationHandler() {
        return handler;
    }

    public abstract void validate();

}

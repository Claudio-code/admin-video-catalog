package com.admin.catalog.domain.validation.handler;

import com.admin.catalog.domain.exceptions.DomainException;
import com.admin.catalog.domain.validation.Error;
import com.admin.catalog.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(Error error) {
        throw DomainException.with(error);
    }

    @Override
    public ValidationHandler append(ValidationHandler validationHandler) {
        throw DomainException.with(validationHandler.getErrors());
    }

    @Override
    public <T> T validate(Validation<T> validation) {
        try {
            return validation.validate();
        } catch (final Exception exception) {
            throw DomainException.with(new Error(exception.getMessage()));
        }
    }

    @Override
    public List<Error> getErrors() {
        return null;
    }

}

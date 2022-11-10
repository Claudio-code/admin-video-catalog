package com.admin.catalog.domain.validation.handler;

import com.admin.catalog.domain.exceptions.DomainException;
import com.admin.catalog.domain.validation.Error;
import com.admin.catalog.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<Error> errors;

    public Notification(final List<Error> errors) {
        this.errors = errors;
    }


    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Throwable throwable) {
        return create(new Error(throwable.getMessage()));
    }

    public static Notification create(final Error error) {
        return new Notification(new ArrayList<>()).append(error);
    }

    @Override
    public Notification append(Error error) {
        errors.add(error);
        return this;
    }

    @Override
    public Notification append(ValidationHandler validationHandler) {
        errors.addAll(validationHandler.getErrors());
        return this;
    }

    @Override
    public ValidationHandler validate(Validation validation) {
        try {
            validation.validate();
        } catch (final DomainException domainException) {
            errors.addAll(domainException.getErrors());
        } catch (final Throwable throwable) {
            errors.add(new Error(throwable.getMessage()));
        }
        return null;
    }

    @Override
    public List<Error> getErrors() {
        return errors;
    }

}

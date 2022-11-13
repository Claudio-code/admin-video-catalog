package com.admin.catalog.domain.validation.handler;

import com.admin.catalog.domain.exceptions.DomainException;
import com.admin.catalog.domain.validation.Error;
import com.admin.catalog.domain.validation.ValidationHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Notification implements ValidationHandler {

    private final List<Error> errors;

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
    public <T> T validate(final Validation<T> validation) {
        try {
            return validation.validate();
        } catch (final DomainException domainException) {
            errors.addAll(domainException.getErrors());
        } catch (final Throwable throwable) {
            errors.add(new Error(throwable.getMessage()));
        }
        return null;
    }

}

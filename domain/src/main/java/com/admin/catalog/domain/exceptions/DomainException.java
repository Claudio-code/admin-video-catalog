package com.admin.catalog.domain.exceptions;

import com.admin.catalog.domain.validation.Error;
import lombok.Getter;

import java.util.List;

@Getter
public class DomainException extends NoStacktraceException {

    protected final List<Error> errors;

    protected DomainException(final String message, final List<Error> anError) {
        super(message);
        errors = anError;
    }

    public static DomainException with(final Error anError) {
        return new DomainException(anError.message(), List.of(anError));
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException("", anErrors);
    }

}

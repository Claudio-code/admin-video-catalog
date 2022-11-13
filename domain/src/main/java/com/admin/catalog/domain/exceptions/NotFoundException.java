package com.admin.catalog.domain.exceptions;

import com.admin.catalog.domain.AggregateRoot;
import com.admin.catalog.domain.Identifier;
import com.admin.catalog.domain.validation.Error;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {

    private static final String ERROR_MESSAGE_TO_FORMAT = "%s with ID %s was not found";

    protected NotFoundException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException with(final Class<? extends AggregateRoot<?>> aggregate, final Identifier id) {
        final var anError = ERROR_MESSAGE_TO_FORMAT.formatted(aggregate.getSimpleName(), id.getValue());
        return new NotFoundException(anError, Collections.emptyList());
    }

}

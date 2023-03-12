package com.admin.catalog.domain.exceptions;

public class InternalErrorException extends NoStacktraceException {

    public InternalErrorException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public static InternalErrorException with(final String message, final Throwable throwable) {
        return new InternalErrorException(message, throwable);
    }

}

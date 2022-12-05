package com.admin.catalog.infrastructure.validation;

import com.admin.catalog.infrastructure.exceptions.ValidatorException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static javax.validation.Validation.buildDefaultValidatorFactory;

public abstract class ModelValidator<T> {

    private final static String MESSAGE_SEPARATOR = ", ";

    private final Validator validator;

    public ModelValidator() {
        validator = buildDefaultValidatorFactory()
            .getValidator();
    }

    public void validate(final T model, final Class<T> modelType) throws ValidatorException {
         final var allErrorMessageOptional = validator.validate(model, modelType)
             .stream()
             .map(ConstraintViolation::getMessage)
             .reduce((aggregate, errorMessage) -> aggregate
                 .concat(MESSAGE_SEPARATOR)
                 .concat(errorMessage));

         if (allErrorMessageOptional.isEmpty()) {
             return;
         }
         throw new ValidatorException(allErrorMessageOptional.get(), modelType.getName());
    }

}

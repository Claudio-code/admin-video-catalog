package com.admin.catalog.infrastructure.validation;

import com.admin.catalog.domain.exceptions.ValidatorException;
import com.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static javax.validation.Validation.buildDefaultValidatorFactory;

public interface ModelValidator<T> {

    default Validator buildValidator() {
        return buildDefaultValidatorFactory()
            .getValidator();
    }

    default void validate(final CategoryJpaEntity model, final Class<T> modelType) throws ValidatorException {
        final var MESSAGE_SEPARATOR = ", ";
        final var allErrorMessageOptional = buildValidator().validate(model)
                .stream()
                .map(ConstraintViolation::getMessage)
                .reduce((aggregate, errorMessage) -> aggregate
                        .concat(MESSAGE_SEPARATOR)
                        .concat(errorMessage));

        if (allErrorMessageOptional.isEmpty()) {
            return;
        }
        throw new ValidatorException(allErrorMessageOptional.get(), modelType.getSimpleName());
    }

}

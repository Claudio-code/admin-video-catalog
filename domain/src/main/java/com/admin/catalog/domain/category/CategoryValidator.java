package com.admin.catalog.domain.category;

import com.admin.catalog.domain.validation.Error;
import com.admin.catalog.domain.validation.ValidationHandler;
import com.admin.catalog.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private static final int NAME_MAX_LENGTH = 255;
    private static final int NAME_MIN_LENGTH = 3;
    private final Category category;

    public CategoryValidator(final Category aCategory, final ValidationHandler handler) {
        super(handler);
        category = aCategory;
    }

    @Override
    public void validate() {
        checkNameConstraints();
        checkDefaultDates();
    }

    private void checkNameConstraints() {
        final var name = category.getName();
        if (name == null) {
            validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (name.isBlank()) {
            validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final var length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            validationHandler().append(new Error("'name' should be between 3 and 255 characters."));
        }
    }

    private void checkDefaultDates() {
        if (category.getCreatedAt() == null) {
            validationHandler().append(new Error("'createdAt' should not be null"));
            return;
        }

        if (category.getUpdatedAt() == null) {
            validationHandler().append(new Error("'updatedAt' should not be null"));
        }
    }

}

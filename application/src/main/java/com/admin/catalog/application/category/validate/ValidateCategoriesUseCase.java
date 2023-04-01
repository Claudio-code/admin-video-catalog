package com.admin.catalog.application.category.validate;

import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.validation.Error;
import com.admin.catalog.domain.validation.ValidationHandler;
import com.admin.catalog.domain.validation.handler.Notification;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ValidateCategoriesUseCase {

    private final CategoryGateway categoryGateway;

    public ValidationHandler validateCategories(final List<CategoryID> ids) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = categoryGateway.existsByIds(ids);
        if (ids.size() != retrievedIds.size()) {
            final var categoryIdsErrorMessage = aggregateMissingIds(ids, retrievedIds);
            notification.append(new Error(categoryIdsErrorMessage));
        }
        return notification;
    }

    private String aggregateMissingIds(final List<CategoryID> missingIds, final List<CategoryID> retrievedIds) {
        missingIds.removeAll(retrievedIds);
        final var missingIdsMessage = missingIds.stream()
            .map(CategoryID::getValue)
            .collect(Collectors.joining(", "));
        return "Some categories could not be found: %s".formatted(missingIdsMessage);
    }

}

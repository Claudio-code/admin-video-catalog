package com.admin.catalog.application.category.retrieve.get;

import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    @Override
    public CategoryOutput execute(final String aCategoryId) {
        final var categoryId = CategoryID.from(aCategoryId);
        return categoryGateway.findById(categoryId)
            .map(CategoryOutput::from)
            .orElseThrow(NotFoundException.notFoundSupplier(categoryId, Category.class));
    }

}

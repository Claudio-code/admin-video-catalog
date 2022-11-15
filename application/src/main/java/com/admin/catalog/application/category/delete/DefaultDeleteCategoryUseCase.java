package com.admin.catalog.application.category.delete;

import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategoryID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {

    private final CategoryGateway categoryGateway;

    @Override
    public void execute(final String aCategoryId) {
        final var categoryId = CategoryID.from(aCategoryId);
        categoryGateway.deletedById(categoryId);
    }

}

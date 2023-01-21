package com.admin.catalog.infrastructure.category.presenters;

import com.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.admin.catalog.application.category.retrieve.list.CategoryListOutput;
import com.admin.catalog.infrastructure.category.models.CategoryListResponse;
import com.admin.catalog.infrastructure.category.models.CategoryResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CategoryApiPresenter {

    static CategoryResponse present(final CategoryOutput categoryOutput) {
        return CategoryResponse.builder()
                .id(categoryOutput.id().getValue())
                .name(categoryOutput.name())
                .description(categoryOutput.description())
                .active(categoryOutput.isActive())
                .createdAt(categoryOutput.createdAt())
                .updatedAt(categoryOutput.updatedAt())
                .deletedAt(categoryOutput.deletedAt())
                .build();
    }

    static CategoryListResponse present(final CategoryListOutput categoryListOutput) {
        return CategoryListResponse.builder()
                .id(categoryListOutput.categoryID().getValue())
                .name(categoryListOutput.name())
                .description(categoryListOutput.description())
                .active(categoryListOutput.isActive())
                .createdAt(categoryListOutput.createdAt())
                .deletedAt(categoryListOutput.deletedAt())
                .build();
    }

}

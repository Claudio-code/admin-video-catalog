package com.admin.catalog.application.category.retrieve.list;

import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.pagination.Pagination;
import com.admin.catalog.domain.pagination.SearchQuery;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultListCategoryUseCase extends ListCategoryUseCase {

    private final CategoryGateway categoryGateway;

    @Override
    public Pagination<CategoryListOutput> execute(final SearchQuery searchQuery) {
        return categoryGateway.findAll(searchQuery)
            .map(CategoryListOutput::from);
    }

}

package com.admin.catalog.application.category.retrieve.list;

import com.admin.catalog.application.UseCase;
import com.admin.catalog.domain.pagination.Pagination;
import com.admin.catalog.domain.pagination.SearchQuery;

public abstract class ListCategoryUseCase extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}

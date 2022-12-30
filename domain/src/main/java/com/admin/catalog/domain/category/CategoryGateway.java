package com.admin.catalog.domain.category;

import com.admin.catalog.domain.exceptions.ValidatorException;
import com.admin.catalog.domain.pagination.Pagination;
import com.admin.catalog.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {

    Category create(Category aCategory) throws ValidatorException;

    void deletedById(CategoryID anId);

    Optional<Category> findById(CategoryID anId);

    Category update(Category aCategory) throws ValidatorException;

    Pagination<Category> findAll(SearchQuery aQuery);

    List<CategoryID> existsByIds(Iterable<CategoryID> ids);

}

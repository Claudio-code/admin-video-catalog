package com.admin.catalog.infrastructure.api.controller;

import com.admin.catalog.application.category.create.CreateCategoryCommand;
import com.admin.catalog.application.category.create.CreateCategoryOutput;
import com.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalog.application.category.retrieve.list.ListCategoryUseCase;
import com.admin.catalog.application.category.update.UpdateCategoryCommand;
import com.admin.catalog.application.category.update.UpdateCategoryOutput;
import com.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.admin.catalog.domain.pagination.Pagination;
import com.admin.catalog.domain.pagination.SearchQuery;
import com.admin.catalog.domain.validation.handler.Notification;
import com.admin.catalog.infrastructure.api.CategoryApi;
import com.admin.catalog.infrastructure.category.models.CategoryListResponse;
import com.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
import com.admin.catalog.infrastructure.category.presenters.CategoryApiPresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
class CategoryController implements CategoryApi {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final ListCategoryUseCase listCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;


    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryRequest input) {
        final var command = CreateCategoryCommand.with(input.name(), input.description(), input.active());
        final Function<Notification, ResponseEntity<?>> onError = notification ->
            ResponseEntity.unprocessableEntity().body(notification);
        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
            ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);
        return createCategoryUseCase.execute(command)
            .fold(onError, onSuccess);
    }

    @Override
    public Pagination<CategoryListResponse> listCategories(
        final String search,
        final int page,
        final int perPage,
        final String sort,
        final String direction
    ) {
        final var query = new SearchQuery(page, perPage, search, sort, direction);
        return listCategoryUseCase.execute(query)
            .map(CategoryApiPresenter::present);
    }

    @Override
    public CategoryResponse getById(String id) {
        final var categoryOutput = getCategoryByIdUseCase.execute(id);
        return CategoryApiPresenter.present(categoryOutput);
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryRequest input) {
        final var command = UpdateCategoryCommand.with(id, input.name(), input.description(), input.active());
        final Function<Notification, ResponseEntity<?>> onError = notification ->
            ResponseEntity.unprocessableEntity().body(notification);
        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;
        return updateCategoryUseCase.execute(command)
            .fold(onError, onSuccess);
    }

    @Override
    public void deleteById(final String id) {
        deleteCategoryUseCase.execute(id);
    }

}

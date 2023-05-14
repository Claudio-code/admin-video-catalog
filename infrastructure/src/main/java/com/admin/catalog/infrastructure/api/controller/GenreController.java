package com.admin.catalog.infrastructure.api.controller;

import com.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.admin.catalog.application.genre.create.CreateGenreCommand;
import com.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.admin.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.admin.catalog.application.genre.retrieve.list.ListGenreUseCase;
import com.admin.catalog.application.genre.update.UpdateGenreCommand;
import com.admin.catalog.application.genre.update.UpdateGenreUseCase;
import com.admin.catalog.domain.pagination.Pagination;
import com.admin.catalog.domain.pagination.SearchQuery;
import com.admin.catalog.infrastructure.api.GenreApi;
import com.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.admin.catalog.infrastructure.genre.models.GenreListResponse;
import com.admin.catalog.infrastructure.genre.models.GenreResponse;
import com.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import com.admin.catalog.infrastructure.genre.presenters.GenreApiPresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class GenreController implements GenreApi {

    private final CreateGenreUseCase createGenreUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListGenreUseCase listGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        final var aCommand = CreateGenreCommand.with(input.name(), input.isActive(), input.categories());
        final var output = createGenreUseCase.execute(aCommand);
        final var uri = URI.create("/genres/" + output.id());
        return ResponseEntity
            .created(uri)
            .body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(
        final String search,
        final int page,
        final int perPage,
        final String sort,
        final String direction
    ) {
        final var aSearchQuery = new SearchQuery(page, perPage, search, sort, direction);
        return listGenreUseCase.execute(aSearchQuery)
            .map(GenreApiPresenter::present);
    }

    @Override
    public GenreResponse getById(final String id) {
        final var output = getGenreByIdUseCase.execute(id);
        return GenreApiPresenter.present(output);
    }

    @Override
    public ResponseEntity<?> updatedById(final String id, final UpdateGenreRequest input) {
        final var aCommand = UpdateGenreCommand.with(id, input.name(), input.isActive(), input.categories());
        final var output = updateGenreUseCase.execute(aCommand);
        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {
        deleteCategoryUseCase.execute(id);
    }

}

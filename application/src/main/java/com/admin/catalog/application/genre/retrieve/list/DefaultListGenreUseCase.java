package com.admin.catalog.application.genre.retrieve.list;

import com.admin.catalog.domain.genre.GenreGateway;
import com.admin.catalog.domain.pagination.Pagination;
import com.admin.catalog.domain.pagination.SearchQuery;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultListGenreUseCase extends ListGenreUseCase {

    private final GenreGateway genreGateway;

    @Override
    public Pagination<GenreListOutput> execute(SearchQuery aIn) {
        return genreGateway.findAll(aIn)
            .map(GenreListOutput::from);
    }

}

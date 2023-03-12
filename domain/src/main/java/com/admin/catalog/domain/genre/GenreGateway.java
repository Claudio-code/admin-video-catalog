package com.admin.catalog.domain.genre;

import java.util.List;
import java.util.Optional;

import com.admin.catalog.domain.pagination.Pagination;
import com.admin.catalog.domain.pagination.SearchQuery;

public interface GenreGateway {

    Genre create(Genre aGenre);

    void deleteById(GenreID anId);

    Optional<Genre> findById(GenreID anId);

    Genre update(Genre aGenre);

    Pagination<Genre> findAll(SearchQuery aQuery);

    List<GenreID> existsByIds(Iterable<GenreID> ids);

}

package com.admin.catalog.infrastructure.genre.application.retrieve.list;

import com.admin.catalog.application.genre.retrieve.list.GenreListOutput;
import com.admin.catalog.application.genre.retrieve.list.ListGenreUseCase;
import com.admin.catalog.domain.genre.Genre;
import com.admin.catalog.domain.genre.GenreGateway;
import com.admin.catalog.domain.pagination.SearchQuery;
import com.admin.catalog.infrastructure.IntegrationTest;
import com.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import com.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
class ListGenreUseCaseIT {

    @Autowired
    private ListGenreUseCase useCase;
    @Autowired
    private GenreGateway genreGateway;
    @Autowired
    private GenreRepository genreRepository;

    @Test
    void givenAValidQuery_whenCallsListGenre_shouldReturnGenres() {
        final var genres = List.of(
            Genre.newGenre("Adventure", true),
            Genre.newGenre("Action", true)
        );
        genreRepository.saveAll(
            genres.stream()
                .map(GenreJpaEntity::from)
                .toList());
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;
        final var expectedItems = genres.stream()
            .map(GenreListOutput::from)
            .toList();
        final var aQuery = new SearchQuery(
            expectedPage,
            expectedPerPage,
            expectedTerms,
            expectedSort,
            expectedDirection
        );
        final var actualOutput = useCase.execute(aQuery);

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems.size(), actualOutput.items().size());
        assertTrue(expectedItems.containsAll(actualOutput.items()));
    }

    @Test
    void givenAValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;
        final var expectedItems = List.<GenreListOutput>of();
        final var aQuery = new SearchQuery(
            expectedPage,
            expectedPerPage,
            expectedTerms,
            expectedSort,
            expectedDirection
        );
        final var actualOutput = useCase.execute(aQuery);

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());
    }

}

package com.admin.catalog.infrastructure.genre.application.delete;

import com.admin.catalog.application.genre.delete.DeleteGenreUseCase;
import com.admin.catalog.domain.genre.Genre;
import com.admin.catalog.domain.genre.GenreGateway;
import com.admin.catalog.domain.genre.GenreID;
import com.admin.catalog.infrastructure.IntegrationTest;
import com.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
class DeleteGenreUseCaseIT {

    @Autowired
    private DeleteGenreUseCase useCase;
    @Autowired
    private GenreGateway genreGateway;
    @Autowired
    private GenreRepository genreRepository;

    @Test
    void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        final var aGenre = genreGateway.create(Genre.newGenre("Action", true));
        final var expectedId = aGenre.getId();

        assertEquals(1, genreRepository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        assertEquals(0, genreRepository.count());
    }

    @Test
    void givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        genreGateway.create(Genre.newGenre("Action", true));
        final var expectedId = GenreID.from("123");

        assertEquals(1, genreRepository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        assertEquals(1, genreRepository.count());
    }

}

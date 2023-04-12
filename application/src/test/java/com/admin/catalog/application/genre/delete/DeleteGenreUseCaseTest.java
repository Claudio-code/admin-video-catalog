package com.admin.catalog.application.genre.delete;

import com.admin.catalog.application.UseCaseTest;
import com.admin.catalog.domain.genre.Genre;
import com.admin.catalog.domain.genre.GenreGateway;
import com.admin.catalog.domain.genre.GenreID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DeleteGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    DefaultDeleteGenreUseCase useCase;

    @Mock
    GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        final var aGenre = Genre.newGenre("Action", true);
        final var expectedId = aGenre.getId();

        doNothing()
            .when(genreGateway).deleteById(expectedId);

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        final var expectedId = GenreID.from("12");

        doNothing()
            .when(genreGateway).deleteById(expectedId);

        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAValidGenreId_whenCallsDeleteGenreAndGatewayThrowsUnexpectedError_shouldReceiveException() {
        final var aGenre = Genre.newGenre("Action", true);
        final var expectedId = aGenre.getId();

        doThrow(new IllegalStateException("Gateway Error"))
            .when(genreGateway).deleteById(expectedId);

        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        verify(genreGateway, times(1)).deleteById(expectedId);
    }

}

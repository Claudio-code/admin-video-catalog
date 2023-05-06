package com.admin.catalog.application.genre.retrieve.get;

import com.admin.catalog.application.UseCaseTest;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.exceptions.NotFoundException;
import com.admin.catalog.domain.genre.Genre;
import com.admin.catalog.domain.genre.GenreGateway;
import com.admin.catalog.domain.genre.GenreID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetGenreByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;
    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    void givenAValidId_whenCallsGetGenre_shouldReturnGenre() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
            CategoryID.from("123"),
            CategoryID.from("321")
        );

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories);
        final var expectedId = aGenre.getId();

        when(genreGateway.findById(Mockito.any()))
            .thenReturn(Optional.of(aGenre));
        final var actualGenre = useCase.execute(expectedId.getValue());

        assertEquals(expectedId.getValue(), actualGenre.id());
        assertEquals(expectedName, actualGenre.name());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(asString(expectedCategories), actualGenre.categories());
        assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());

        verify(genreGateway, times(1)).findById(expectedId);
    }

    @Test
    void givenAValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        when(genreGateway.findById(expectedId))
            .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> {
            useCase.execute(expectedId.getValue());
        });
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}

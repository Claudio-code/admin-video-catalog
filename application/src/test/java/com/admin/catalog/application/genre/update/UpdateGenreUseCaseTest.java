package com.admin.catalog.application.genre.update;

import com.admin.catalog.application.UseCaseTest;
import com.admin.catalog.application.category.validate.ValidateCategoriesUseCase;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.genre.Genre;
import com.admin.catalog.domain.genre.GenreGateway;
import com.admin.catalog.domain.validation.handler.Notification;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;
    @Mock
    private GenreGateway gateway;

    @Mock
    ValidateCategoriesUseCase validateCategoriesUseCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway, validateCategoriesUseCase);
    }

    @Test
    void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var aGenre = Genre.newGenre("Action", true);
        final var expectedId = aGenre.getId();
        final var expectedName = aGenre.getName();
        final var expectedIsActive = aGenre.isActive();
        final var expectedCategories = List.<CategoryID>of();
        final var aCommand = UpdateGenreCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        );

        when(validateCategoriesUseCase.validateCategories(any()))
            .thenReturn(Notification.create());
        when(gateway.findById(any()))
            .thenReturn(Optional.of(Genre.with(aGenre)));
        when(gateway.update(any()))
            .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        verify(gateway, times(1)).findById(expectedId);
        verify(gateway, times(1)).update(Mockito.argThat(aUpdateGenre ->
            Objects.equals(expectedId, aUpdateGenre.getId())
                && Objects.equals(expectedName, aUpdateGenre.getName())
                && Objects.equals(expectedIsActive, aUpdateGenre.isActive())
                && Objects.equals(expectedCategories, aUpdateGenre.getCategories())
                && Objects.equals(aGenre.getCreatedAt(), aUpdateGenre.getCreatedAt())
                && aGenre.getUpdatedAt().isBefore(aUpdateGenre.getUpdatedAt())
                && Objects.isNull(aUpdateGenre.getDeletedAt())));
    }

    @Test
    void givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var aGenre = Genre.newGenre("Action", true);
        final var expectedId = aGenre.getId();
        final var expectedName = aGenre.getName();
        final var expectedIsActive = aGenre.isActive();
        final var expectedCategories = List.of(
            CategoryID.from("123"),
            CategoryID.from("321")
        );
        final var aCommand = UpdateGenreCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        );

        when(validateCategoriesUseCase.validateCategories(any()))
            .thenReturn(Notification.create());
        when(gateway.findById(any()))
            .thenReturn(Optional.of(Genre.with(aGenre)));
        when(gateway.update(any()))
            .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        verify(gateway, times(1)).findById(any());
        verify(gateway, times(1)).update(argThat(aUpdatedGenre ->
            Objects.equals(expectedId, aUpdatedGenre.getId())
                && Objects.equals(expectedName, aUpdatedGenre.getName())
                && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
                && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
                && Objects.isNull(aUpdatedGenre.getDeletedAt())));
    }


    @Test
    void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var aGenre = Genre.newGenre("Action", true);
        final var expectedId = aGenre.getId();
        final var expectedName = aGenre.getName();
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();
        final var aCommand = UpdateGenreCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        );

        when(validateCategoriesUseCase.validateCategories(any()))
            .thenReturn(Notification.create());
        when(gateway.findById(any()))
            .thenReturn(Optional.of(Genre.with(aGenre)));
        when(gateway.update(any()))
            .thenAnswer(returnsFirstArg());

        assertTrue(aGenre.isActive());
        assertNull(aGenre.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        verify(validateCategoriesUseCase, times(1))
            .validateCategories(expectedCategories);
        verify(gateway, times(1)).findById(expectedId);
        verify(gateway, times(1)).update(argThat(aUpdateGenre ->
            Objects.equals(expectedId, aUpdateGenre.getId())
                && Objects.equals(expectedName, aUpdateGenre.getName())
                && Objects.equals(expectedIsActive, aUpdateGenre.isActive())
                && Objects.equals(expectedCategories, aUpdateGenre.getCategories())
                && Objects.equals(aGenre.getCreatedAt(), aUpdateGenre.getCreatedAt())
                && aGenre.getUpdatedAt().isBefore(aUpdateGenre.getUpdatedAt())
                && Objects.nonNull(aUpdateGenre.getDeletedAt())));
    }

}

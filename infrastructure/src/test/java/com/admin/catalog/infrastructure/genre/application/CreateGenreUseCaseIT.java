package com.admin.catalog.infrastructure.genre.application;

import com.admin.catalog.application.genre.create.CreateGenreCommand;
import com.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.exceptions.ValidatorException;
import com.admin.catalog.domain.genre.GenreGateway;
import com.admin.catalog.infrastructure.IntegrationTest;
import com.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
class CreateGenreUseCaseIT {

    @Autowired
    private CreateGenreUseCase useCase;
    @SpyBean
    private CategoryGateway categoryGateway;
    @SpyBean
    private GenreGateway genreGateway;
    @Autowired
    private GenreRepository genreRepository;

    @Test
    void givenAValidCommand_whenCreateGenre_shouldReturnGenreId() throws ValidatorException {
        final var movie = categoryGateway.create(Category.newCategory("Action", null, true));
        final var expectedName = "Movies";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movie.getId());
        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));
        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        assertTrue(expectedCategories.containsAll(actualGenre.getCategories()));
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
        final var expectedName = "Movies";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();
        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));
        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNotNull(actualGenre.getDeletedAt());
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
            .map(CategoryID::getValue)
            .toList();
    }

}

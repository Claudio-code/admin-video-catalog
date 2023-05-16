package com.admin.catalog.infrastructure.genre.application.update;

import com.admin.catalog.application.genre.update.UpdateGenreCommand;
import com.admin.catalog.application.genre.update.UpdateGenreUseCase;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.genre.Genre;
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
class UpdateGenreUseCaseIT {

    @Autowired
    private UpdateGenreUseCase useCase;
    @SpyBean
    private GenreGateway genreGateway;
    @Autowired
    private GenreRepository genreRepository;

    @Test
    void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        final var aGenre = genreGateway.create(Genre.newGenre("action", true));
        final var expectedId = aGenre.getId();
        final var expectedName = "Actions";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var aCommand = UpdateGenreCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
        );

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertEquals(expectedId.getValue(), actualOutput.id());

        final var actualGenre = genreRepository.findById(aGenre.getId().getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        assertTrue(expectedCategories.containsAll(actualGenre.getCategories()));
        assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
            .map(CategoryID::getValue)
            .toList();
    }

}

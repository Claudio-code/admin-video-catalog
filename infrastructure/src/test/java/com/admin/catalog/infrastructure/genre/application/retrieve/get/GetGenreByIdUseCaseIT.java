package com.admin.catalog.infrastructure.genre.application.retrieve.get;

import com.admin.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.exceptions.NotFoundException;
import com.admin.catalog.domain.exceptions.ValidatorException;
import com.admin.catalog.domain.genre.Genre;
import com.admin.catalog.domain.genre.GenreGateway;
import com.admin.catalog.infrastructure.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
class GetGenreByIdUseCaseIT {

    @Autowired
    private GetGenreByIdUseCase useCase;
    @Autowired
    private CategoryGateway categoryGateway;
    @Autowired
    private GenreGateway genreGateway;

    @Test
    void givenAValidId_whenCallsGetGenre_shouldReturnGenre() throws ValidatorException {
        final var action = categoryGateway.create(Category.newCategory("Action", null, true));
        final var horror = categoryGateway.create(Category.newCategory("Horror", null, true));
        final var expectedName = "Movies";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(action.getId(), horror.getId());
        final var aGenre = genreGateway.create(Genre.newGenre(expectedName, expectedIsActive).addCategories(expectedCategories));
        final var expectedId = aGenre.getId();

        final var actualGenre = useCase.execute(expectedId.getValue());
        assertEquals(expectedId.getValue(), actualGenre.id());
        assertEquals(expectedName, actualGenre.name());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories.size(), actualGenre.categories().size());
        assertTrue(asString(expectedCategories).containsAll(actualGenre.categories()));
    }

    @Test
    void givenAValidId_whenCallsGetGenreAndDoesNotExists_shouldReturnNotFound() {
        final var expectedId = "123";
        final var expectedErrorMessage = "Genre with ID " + expectedId + " was not found";

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(expectedId));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
            .map(CategoryID::getValue)
            .toList();
    }

}

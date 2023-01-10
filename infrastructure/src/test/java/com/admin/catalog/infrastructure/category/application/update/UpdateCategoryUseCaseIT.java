package com.admin.catalog.infrastructure.category.application.update;

import com.admin.catalog.application.category.update.UpdateCategoryCommand;
import com.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.exceptions.NotFoundException;
import com.admin.catalog.domain.exceptions.ValidatorException;
import com.admin.catalog.infrastructure.IntegrationTest;
import com.admin.catalog.infrastructure.category.CategoryMysqlGateway;
import com.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryMysqlGateway gateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory("Movie", null, true);
        save(aCategory);

        final var expectedId = aCategory.getId();
        final var expectedName = "Another Movie";
        final var expectedDescription = "The best category in the word";
        final var expectedIsActive = true;

        final var aCommand = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        assertEquals(1, repository.count());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualCategory = repository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws ValidatorException {
        final var aCategory = Category.newCategory("Movie", null, true);
        save(aCategory);

        final var expectedId = aCategory.getId();
        final String expectedName = null;
        final var expectedDescription = "The best category in the word";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedIsActive
        );
        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstOrError().message());
        verify(gateway, times(0)).update(any());
    }

    @Test
    public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        final var aCategory = Category.newCategory("Movie", null, true);
        save(aCategory);

        final var expectedId = aCategory.getId();
        final var expectedName = "Another Movie";
        final var expectedDescription = "The best category in the word";
        final var expectedIsActive = false;

        final var aCommand = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        assertEquals(1, repository.count());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var actualCategory = repository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnException() throws ValidatorException {
        final var aCategory = Category.newCategory("Serie", null, true);
        save(aCategory);

        final var expectedName = "New movie";
        final var expectedDescription = "The worst movie in all word";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var aCommand = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
            .when(gateway).update(any());

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstOrError().message());

        final var actualCategory = repository.findById(expectedId.getValue()).get();

        assertEquals(aCategory.getName(), actualCategory.getName());
        assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        assertEquals(aCategory.isActive(), actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "New movie";
        final var expectedDescription = "The worst movie in all word";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Category with ID 123 was not found";

        final var aCommand = UpdateCategoryCommand.with(
            "123",
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private void save(final Category... aCategory) {
        final var categories = Stream.of(aCategory)
            .map(CategoryJpaEntity::from)
            .toList();
        repository.saveAllAndFlush(categories);
    }

}

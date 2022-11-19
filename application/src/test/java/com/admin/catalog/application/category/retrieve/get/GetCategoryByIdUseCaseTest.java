package com.admin.catalog.application.category.retrieve.get;

import com.admin.catalog.application.UseCaseTest;
import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class GetCategoryByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    DefaultGetCategoryByIdUseCase useCase;

    @Mock
    CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    void givenAVallidId_whenCallsGetCategory_shouldReturnCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The best movies";
        final var expectedActive = true;

        final var aCategory = Category
            .newCategory(expectedName, expectedDescription, expectedActive);
        final var expectedId = aCategory.getId();

        when(categoryGateway.findById(eq(expectedId)))
            .thenReturn(Optional.of(aCategory.clone()));

        final var actualCategory = useCase.execute(expectedId.getValue());

        assertEquals(expectedId, actualCategory.id());
        assertEquals(expectedActive, actualCategory.isActive());
        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(aCategory.getCreatedAt(), actualCategory.createdAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.updatedAt());
        assertNull(actualCategory.deletedAt());
    }

    @Test
    void givenAInvalid_whenCallsGetCategory_shouldReturnNotFound() {
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.from("123");

        when(categoryGateway.findById(eq(expectedId)))
            .thenReturn(Optional.empty());

        final var actualException = assertThrows(
            NotFoundException.class,
            () -> useCase.execute(expectedId.getValue())
        );

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    void givenAValidId_whenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedErrorMessage = "Gateway error";
        final var expectedId = CategoryID.from("123");

        when(categoryGateway.findById(eq(expectedId)))
            .thenThrow(new RuntimeException(expectedErrorMessage));

        final var actualException = assertThrows(
            RuntimeException.class,
            () -> useCase.execute(expectedId.getValue())
        );

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}

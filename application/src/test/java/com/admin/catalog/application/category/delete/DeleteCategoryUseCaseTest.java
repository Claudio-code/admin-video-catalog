package com.admin.catalog.application.category.delete;

import com.admin.catalog.application.UseCaseTest;
import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategoryID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DeleteCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    DefaultDeleteCategoryUseCase useCase;

    @Mock
    CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }


    @Test
    void givenAValidCategoryId_whenCallsDeleteCategory_shouldBeOk() {
        final var categoryCreated = Category.newCategory("Movies22", null, true);
        final var categoryID = categoryCreated.getId();

        doNothing()
            .when(categoryGateway).deletedById(eq(categoryID));

        assertDoesNotThrow(() -> useCase.execute(categoryID.getValue()));
        verify(categoryGateway, times(1)).deletedById(eq(categoryID));
    }


    @Test
    void givenAInvalidCategoryId_whenCallsDeleteCategory_shouldBeOk() {
        final var categoryID = CategoryID.from("123");

        doNothing()
            .when(categoryGateway).deletedById(eq(categoryID));

        assertDoesNotThrow(() -> useCase.execute(categoryID.getValue()));
        verify(categoryGateway, times(1)).deletedById(eq(categoryID));
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var categoryCreated = Category.newCategory("Movies22", null, true);
        final var categoryID = categoryCreated.getId();
        final var expectedErrorMessage = "Gateway error";

        doThrow(new RuntimeException(expectedErrorMessage))
            .when(categoryGateway).deletedById(eq(categoryID));

        assertThrows(RuntimeException.class, () -> useCase.execute(categoryID.getValue()));
        verify(categoryGateway, times(1)).deletedById(eq(categoryID));
    }

}

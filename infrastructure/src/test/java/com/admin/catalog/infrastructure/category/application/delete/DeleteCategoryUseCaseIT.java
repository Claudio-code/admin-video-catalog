package com.admin.catalog.infrastructure.category.application.delete;

import com.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.infrastructure.IntegrationTest;
import com.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOk() {
        final var aCategory = Category.newCategory("New movie", "Another movie", true);
        final var expectedId = aCategory.getId();

        save(aCategory);

        assertEquals(1, categoryRepository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOk() {
        final var expectedId = CategoryID.from("123");

        assertEquals(0, categoryRepository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var aCategory = Category.newCategory("New movie", "Another movie", true);
        final var expectedId = aCategory.getId();
        final var expectedErrorMessage = "Gateway Error";

        doThrow(new IllegalStateException(expectedErrorMessage))
            .when(categoryGateway).deletedById(any());
        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        verify(categoryGateway, times(1)).deletedById(any());
    }

    private void save(final Category... aCategories) {
        final var categories = Arrays.stream(aCategories)
            .map(CategoryJpaEntity::from)
            .toList();
        categoryRepository.saveAllAndFlush(categories);
    }
}

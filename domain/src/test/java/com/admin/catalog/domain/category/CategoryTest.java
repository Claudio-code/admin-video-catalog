package com.admin.catalog.domain.category;

import com.admin.catalog.domain.exceptions.DomainException;
import com.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CategoryTest {

    @Test
    void givenAValidParamsWhenCallNewCategoryThenInstantiateCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Another movies";
        final var expectedIsActive = true;

        final var actualCategory = Category
                .newCategory(expectedName, expectedDescription, expectedIsActive);


        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
    }

    @Test
    void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedDescription = "Another movies";
        final var expectedIsActive = true;

        final var actualCategory = Category
                .newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(
                DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));


        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage,actualException.getErrors().get(0).message());
    }

}

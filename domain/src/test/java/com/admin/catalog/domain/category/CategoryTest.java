package com.admin.catalog.domain.category;

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
}

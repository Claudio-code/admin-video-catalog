package com.admin.catalog.infrastructure.category.validator;

import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.exceptions.ValidatorException;
import com.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.admin.catalog.infrastructure.category.persistence.CategoryModelValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategoryModelValidatorTest {

    private static CategoryModelValidator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        validator = new CategoryModelValidator();
    }

    @Test
    public void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
        final var expectedMessage = "CategoryJpaEntity: name";
        final var aCategory = Category.newCategory(null, null, true);
        final var aCategoryJpa = CategoryJpaEntity.from(aCategory);

        final var actualException = assertThrows(ValidatorException.class, () -> validator.validate(aCategoryJpa, CategoryJpaEntity.class));
        assertTrue(actualException.getMessage().contains(expectedMessage));
    }

    @Test
    public void givenAnInvalidNullCreatedAt_whenCallsSave_shouldReturnError() {
        final var expectedMessage = "CategoryJpaEntity: createdAt";
        final var aCategory = Category.newCategory("null", null, true);
        final var aCategoryJpa = CategoryJpaEntity.from(aCategory);
        aCategoryJpa.setCreatedAt(null);

        final var actualException = assertThrows(ValidatorException.class, () -> validator.validate(aCategoryJpa, CategoryJpaEntity.class));

        assertTrue(actualException.getMessage().contains(expectedMessage));
    }

    @Test
    public void givenAnInvalidNullUpdatedAt_whenCallsSave_shouldReturnError() {
        final var expectedMessage = "CategoryJpaEntity: updatedAt";
        final var aCategory = Category.newCategory("null", null, true);
        final var aCategoryJpa = CategoryJpaEntity.from(aCategory);
        aCategoryJpa.setUpdatedAt(null);

        final var actualException = assertThrows(ValidatorException.class, () -> validator.validate(aCategoryJpa, CategoryJpaEntity.class));

        assertTrue(actualException.getMessage().contains(expectedMessage));
    }

}


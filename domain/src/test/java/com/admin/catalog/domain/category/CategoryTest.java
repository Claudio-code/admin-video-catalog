package com.admin.catalog.domain.category;

import com.admin.catalog.domain.exceptions.DomainException;
import com.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategoryTest {

    @Test
    void givenAValidParamsWhenCallNewCategoryThenInstantiateCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Another movies";
        final var expectedIsActive = true;

        final var actualCategory = Category
                .newCategory(expectedName, expectedDescription, expectedIsActive);


        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId().toString());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
    }

    @Test
    void givenAValidCategory_whenCallUpdate_thenUpdatedCategoryFields() {
        final var expectedName = "Movies";
        final var expectedDescription = "Another movies";
        final var expectedIsActive = true;

        final var actualCategory = Category
                .newCategory(expectedName, expectedDescription, expectedIsActive);

        assertNotNull(actualCategory.getId().toString());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());


        final var expectedNameUpdate = "Movies";
        final var expectedDescriptionUpdate = "Another movies";
        final var expectedIsActiveUpdate = false;
        actualCategory.update(expectedNameUpdate, expectedDescriptionUpdate, expectedIsActiveUpdate);

        assertEquals(expectedNameUpdate, actualCategory.getName());
        assertEquals(expectedDescriptionUpdate, actualCategory.getDescription());
        assertEquals(expectedIsActiveUpdate, actualCategory.isActive());

        actualCategory.update(expectedNameUpdate, expectedDescriptionUpdate, true);

        assertTrue(actualCategory.isActive());
    }

    @Test
    void givenAValidParamsWhenCallUpdateCategoryThenInstantiateCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Another movies";
        final var expectedIsActive = true;

        final var actualCategory = Category
                .newCategory(expectedName, expectedDescription, expectedIsActive);

        assertEquals(expectedIsActive, actualCategory.isActive());

        actualCategory.deactivate();

        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertFalse(actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenTrueValueInActivateWhenReactiveCategoryThenInstantiateCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Another movies";
        final var expectedIsActive = true;

        final var actualCategory = Category
                .newCategory(expectedName, expectedDescription, expectedIsActive);

        actualCategory.deactivate();
        assertFalse(actualCategory.isActive());

        actualCategory.active();
        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertTrue(actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
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

    @Test
    void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedDescription = "Another movies";
        final var expectedIsActive = true;

        final var actualCategory = Category
                .newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(
                DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));


        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage,actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidMinLengthName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "w";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should be between 3 and 255 characters.";
        final var expectedDescription = "Another movies";
        final var expectedIsActive = true;

        final var actualCategory = Category
                .newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(
                DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));


        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage,actualException.getErrors().get(0).message());
    }

    @Test
    void givenAnInvalidMaxLengthName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "dqwd qwd qwdqwdq wdq wdqofnornopqooqdkqwpo dqkwpd okqwpodk qwoiwfneidqwd qwd qwdqwdq wdq wdqofnornopqooqdkqwpo dqkwpd okqwpodk qwoiwfneidqwd qwd qwdqwdq wdq wdqofnornopqooqdkqwpo dqkwpd okqwpodk qwoiwfneidqwd qwd qwdqwdq wdq wdqofnornopqooqdkqwpo dqkwpd okqwpodk qwoiwfneidqwd qwd qwdqwdq wdq wdqofnornopqooqdkqwpo dqkwpd okqwpodk qwoiwfneidqwd qwd qwdqwdq wdq wdqofnornopqooqdkqwpo dqkwpd okqwpodk qwoiwfneidqwd qwd qwdqwdq wdq wdqofnornopqooqdkqwpo dqkwpd okqwpodk qwoiwfnei";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should be between 3 and 255 characters.";
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

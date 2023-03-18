package com.admin.catalog.domain.genre;

import java.util.ArrayList;
import java.util.List;

import com.admin.catalog.domain.UnitTest;
import com.admin.catalog.domain.category.CategoryID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenreTest extends UnitTest {

    @Test
    void givenValidParams_whenCallsNewGenre_shouldInstantiateGenre() {
        final var expectName = "Action";
        final var expectIsActive = true;
        final var expectCategories = 0;
        final var actualGenre = Genre.newGenre(expectName, expectIsActive);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectName, actualGenre.getName());
        assertEquals(expectIsActive, actualGenre.isActive());
        assertEquals(expectCategories, actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAnActiveGenre_whenCallDeactivate_shouldReceiveOk() {
        final var expectName = "Action";
        final var expectIsActive = false;
        final var expectCategories = 0;
        final var actualGenre = Genre.newGenre(expectName, true);

        assertNotNull(actualGenre);
        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.deactivate();

        assertNotNull(actualGenre.getId());
        assertEquals(expectName, actualGenre.getName());
        assertEquals(expectIsActive, actualGenre.isActive());
        assertEquals(expectCategories, actualGenre.getCategories().size());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAnInactiveGenre_whenCallActivate_shouldReceiveOk() {
        final var expectName = "Action";
        final var expectIsActive = true;
        final var expectCategories = 0;
        final var actualGenre = Genre.newGenre(expectName, false);

        assertNotNull(actualGenre);
        assertFalse(actualGenre.isActive());
        assertNotNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.active();

        assertNotNull(actualGenre.getId());
        assertEquals(expectName, actualGenre.getName());
        assertEquals(expectIsActive, actualGenre.isActive());
        assertEquals(expectCategories, actualGenre.getCategories().size());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAValidInactiveGenre_whenCallUpdateWithActive_shouldReceiveGenreUpdated() {
        final var expectName = "Action";
        final var expectIsActive = true;
        final var actualGenre = Genre.newGenre(expectName, false);

        assertNotNull(actualGenre);
        assertFalse(actualGenre.isActive());
        assertNotNull(actualGenre.getDeletedAt());

        actualGenre.update(expectName.concat(expectName), expectIsActive, null);

        assertNotNull(actualGenre.getId());
        assertEquals(expectName.concat(expectName), actualGenre.getName());
        assertEquals(expectIsActive, actualGenre.isActive());
        assertEquals(0, actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAValidInactiveGenre_whenCallUpdateWithInactive_shouldReceiveGenreUpdated() {
        final var expectName = "Action";
        final var expectIsActive = false;
        final var actualGenre = Genre.newGenre(expectName, true);

        assertNotNull(actualGenre);
        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        actualGenre.update(expectName.concat(expectName), expectIsActive, null);

        assertNotNull(actualGenre.getId());
        assertEquals(expectName.concat(expectName), actualGenre.getName());
        assertEquals(expectIsActive, actualGenre.isActive());
        assertEquals(0, actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAValidActiveGenre_whenCallUpdateWithInactivate_shouldReceiveGenreUpdated() {
        final var expectName = "Action";
        final var expectIsActive = false;
        final var actualGenre = Genre.newGenre(expectName, true);
        final var expectedCategories = List.of(CategoryID.from("123"));

        assertNotNull(actualGenre);
        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        actualGenre.update(expectName.concat(expectName), expectIsActive, expectedCategories);

        assertNotNull(actualGenre.getId());
        assertEquals(expectName.concat(expectName), actualGenre.getName());
        assertEquals(expectIsActive, actualGenre.isActive());
        assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNotNull(actualGenre.getDeletedAt());

    }

    @Test
    void givenAValidGenre_whenCallUpdateWithNullCategories_shouldReceiveOk() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        assertDoesNotThrow(() -> actualGenre.update(expectedName, expectedIsActive, null));
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt.toString(), actualGenre.getCreatedAt().toString());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAValidEmptyCategoriesGenre_whenCallAddCategory_shouldReceiveOk() {
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("321");
        final var expectName = "Action";
        final var expectIsActive = true;
        final var expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre(expectName, expectIsActive);
        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        assertEquals(0, actualGenre.getCategories().size());

        actualGenre
            .addCategory(seriesID)
            .addCategory(moviesID);

        assertNotNull(actualGenre);
        assertEquals(expectName, actualGenre.getName());
        assertEquals(expectIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAInvalidNullAsCategoryID_whenCallAddCategory_shouldReceiveOK() {
        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        assertEquals(0, actualGenre.getCategories().size());

        actualGenre.addCategory(null);

        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

}

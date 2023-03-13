package com.admin.catalog.domain.genre;

import com.admin.catalog.domain.UnitTest;
import org.junit.jupiter.api.Test;

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

}

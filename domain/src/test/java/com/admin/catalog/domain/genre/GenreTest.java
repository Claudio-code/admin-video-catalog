package com.admin.catalog.domain.genre;

import com.admin.catalog.domain.UnitTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void givenAnActiveGenre_whenCallDeactive_shouldReceiveOk() {
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

}

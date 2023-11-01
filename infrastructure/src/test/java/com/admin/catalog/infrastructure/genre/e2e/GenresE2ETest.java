package com.admin.catalog.infrastructure.genre.e2e;

import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.infrastructure.E2ETest;
import com.admin.catalog.infrastructure.MockDsl;
import com.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class GenresE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private GenreRepository genreRepository;
    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:latest")
        .withPassword("123456")
        .withUsername("root")
        .withDatabaseName("catalog");

    @Override
    public MockMvc mvc() {
        return mvc;
    }

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToCreateAnewGenreWithValidValues() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final List<CategoryID> expectedCategories = List.of();

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);
        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(
            expectedCategories.size() == actualGenre.getCategories().size()
                && expectedCategories.containsAll(actualGenre.getCategories())
        );
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToCreateANewGenreWithCategories() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        final var expectedName = "Horror";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(givenACategory("Hard work", null, true));
        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);
        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertTrue(
            expectedCategories.size() == actualGenre.getCategories().size()
                && expectedCategories.containsAll(actualGenre.getCategories())
        );
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToNavigateThruAllGenres() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        givenAGenre("Action", true, List.of());
        givenAGenre("Horror", true, List.of());
        givenAGenre("Sport", true, List.of());

        listGenres(0, 1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currentPage", equalTo(0)))
            .andExpect(jsonPath("$.perPage", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.items", hasSize(1)))
            .andExpect(jsonPath("$.items[0].name", equalTo("Action")));

        listGenres(1, 1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currentPage", equalTo(1)))
            .andExpect(jsonPath("$.perPage", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.items", hasSize(1)))
            .andExpect(jsonPath("$.items[0].name", equalTo("Horror")));

        listGenres(2, 1)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currentPage", equalTo(2)))
            .andExpect(jsonPath("$.perPage", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.items", hasSize(1)))
            .andExpect(jsonPath("$.items[0].name", equalTo("Sport")));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToSearchBetweenAllGenres() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        givenAGenre("Action", true, List.of());
        givenAGenre("Horror", true, List.of());
        givenAGenre("Sport", true, List.of());

        listGenres(0, 1, "Hor")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currentPage", equalTo(0)))
            .andExpect(jsonPath("$.perPage", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(1)))
            .andExpect(jsonPath("$.items", hasSize(1)))
            .andExpect(jsonPath("$.items[0].name", equalTo("Horror")));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToSortAllGenresByNameDesc() throws Exception {
        assertTrue(MYSQL_CONTAINER.isRunning());
        assertEquals(0, genreRepository.count());

        givenAGenre("Action", true, List.of());
        givenAGenre("Horror", true, List.of());
        givenAGenre("Sport", true, List.of());

        listGenres(0, 3, "", "name", "desc")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currentPage", equalTo(0)))
            .andExpect(jsonPath("$.perPage", equalTo(3)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.items", hasSize(3)))
            .andExpect(jsonPath("$.items[0].name", equalTo("Action")))
            .andExpect(jsonPath("$.items[1].name", equalTo("Horror")))
            .andExpect(jsonPath("$.items[2].name", equalTo("Sport")));
    }

}

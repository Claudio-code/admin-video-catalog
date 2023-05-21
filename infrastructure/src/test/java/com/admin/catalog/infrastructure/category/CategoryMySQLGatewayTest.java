package com.admin.catalog.infrastructure.category;

import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.exceptions.ValidatorException;
import com.admin.catalog.domain.pagination.SearchQuery;
import com.admin.catalog.infrastructure.MySQLGatewayTest;
import com.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MySQLGatewayTest
class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMysqlGateway categoryMysqlGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() throws ValidatorException {
        final var expectedName = "Movies";
        final var expectedDescription = "The category";
        final var expectedIsActive = true;
        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryMysqlGateway.create(aCategory);

        assertEquals(1, categoryRepository.count());

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertNull(actualCategory.getDeletedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());

        final var actualEntity = categoryRepository.findById(actualCategory.getId().getValue()).get();

        assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(expectedDescription, actualEntity.getDescription());
        assertEquals(expectedIsActive, actualEntity.isActive());
        assertNull(actualEntity.getDeletedAt());
        assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
    }

    @Test
    void givenAValidCategory_whenCallUpdate_shouldReturnCategoryUpdated() throws ValidatorException {
        final var expectedName = "another movie";
        final var expectedActualName = "My movie";
        final var expectedDescription = "another end of year";
        final var expectedIsActive = true;
        final var aCategory = Category.newCategory(expectedActualName, null, expectedIsActive);

        assertEquals(0, categoryRepository.count());
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        assertEquals(1, categoryRepository.count());

        final var actualInvalidEntity = categoryRepository.findById(aCategory.getId().getValue()).get();
        assertEquals(expectedActualName, actualInvalidEntity.getName());
        assertNull(actualInvalidEntity.getDescription());
        assertEquals(expectedIsActive, actualInvalidEntity.isActive());

        final var aUpdatedCategory = aCategory.clone()
                .update(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = categoryMysqlGateway.update(aUpdatedCategory);

        assertEquals(1, categoryRepository.count());
        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(actualCategory.getId().getValue()).get();

        assertEquals(1, categoryRepository.count());
        assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedDescription, actualEntity.getDescription());
        assertEquals(expectedIsActive, actualEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
        assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenInvalidCategoryId_whenTryDeleteIt_shouldDeleteCategory() {
        assertEquals(0, categoryRepository.count());
        categoryMysqlGateway.deletedById(CategoryID.from("INVALID"));
        assertEquals(0, categoryRepository.count());
    }

    @Test
    void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeletedCategory() {
        final var aCategory = Category.newCategory("Movie", null, true);

        assertEquals(0, categoryRepository.count());
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        assertEquals(1, categoryRepository.count());
        categoryMysqlGateway.deletedById(aCategory.getId());
        assertEquals(0, categoryRepository.count());
    }

    @Test
    void givenAPrePersistedCategoryAndValidCategoryId_whenCallFindById_shouldReturnCategory() {
        final var expectedName = "Series";
        final var expectedDescription = "New bigger success";
        final var expectedIsActive = true;
        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertEquals(0, categoryRepository.count());
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        assertEquals(1, categoryRepository.count());

        final var actualCategory = categoryMysqlGateway.findById(aCategory.getId()).get();

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenValidCategoryIdNotStored_whenCallsFindById_shouldReturnEmpty() {
        assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryMysqlGateway.findById(CategoryID.from("Invalid"));

        assertTrue(actualCategory.isEmpty());
    }

    @Test
    void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentaries = Category.newCategory("Documentaries", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAllAndFlush(List.of(
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(movies),
            CategoryJpaEntity.from(documentaries)
        ));

        assertEquals(expectedTotal, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "", "name", "asc");
        final var actualResult = categoryMysqlGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(documentaries.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void givenEmptyCategoriesTable_whenCallsFindAll_shouldEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        final var query = new SearchQuery(0, 1, "", "name", "asc");
        final var actualResult = categoryMysqlGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertTrue(actualResult.items().isEmpty());
    }

    @Test
    void givenFollowPagination_whenCallsFindWithPagel_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentaries = Category.newCategory("Documentaries", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAllAndFlush(List.of(
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(movies),
            CategoryJpaEntity.from(documentaries)
        ));

        assertEquals(expectedTotal, categoryRepository.count());

        var query = new SearchQuery(0, 1, "", "name", "asc");
        var actualResult = categoryMysqlGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(documentaries.getId(), actualResult.items().get(0).getId());

        // 1 page
        expectedPage = 1;
        query = new SearchQuery(1, 1, "", "name", "asc");
        actualResult = categoryMysqlGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(movies.getId(), actualResult.items().get(0).getId());

        // 2 page
        expectedPage = 2;
        query = new SearchQuery(2, 1, "", "name", "asc");
        actualResult = categoryMysqlGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(series.getId(), actualResult.items().get(0).getId());

        // 3 page empty
        expectedPage = 3;
        query = new SearchQuery(3, 1, "", "name", "asc");
        actualResult = categoryMysqlGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertTrue(actualResult.items().isEmpty());
    }

    @Test
    void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchCategoryName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentaries = Category.newCategory("Documentaries", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAllAndFlush(List.of(
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(movies),
            CategoryJpaEntity.from(documentaries)
        ));

        final var query = new SearchQuery(0, 1, "doc", "name", "asc");
        final var actualResult = categoryMysqlGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(documentaries.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void givenPrePersistedCategories_whenCallsExistsByIds_shouldReturnIds() {
        final var movies = Category.newCategory("Movies", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentaries = Category.newCategory("Documentaries", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAllAndFlush(List.of(
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(movies),
            CategoryJpaEntity.from(documentaries)
        ));

        assertEquals(3, categoryRepository.count());

        final var expectedIds = List.of(movies.getId(), series.getId());
        final var ids = List.of(movies.getId(), series.getId(), CategoryID.from("123"));

        final var actualResults = categoryMysqlGateway.existsByIds(ids);

        assertEquals(expectedIds.size(), actualResults.size());
        assertTrue(expectedIds.containsAll(actualResults));
    }

}

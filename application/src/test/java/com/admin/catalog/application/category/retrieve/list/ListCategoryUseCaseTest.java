package com.admin.catalog.application.category.retrieve.list;

import com.admin.catalog.application.UseCaseTest;
import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.pagination.Pagination;
import com.admin.catalog.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ListCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    DefaultListCategoryUseCase useCase;

    @Mock
    CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }


    @Test
    void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories() {
        final var expectedCategoryOne = Category.newCategory("My category", null, true);
        final var expectedCategoryTwo = Category.newCategory("My category 2", null, true);
        final var categories = List.of(expectedCategoryOne, expectedCategoryTwo);
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);
        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);
        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(categories.size(), actualResult.total());
    }

    @Test
    void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyCategories() {
        final var categories = List.<Category>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);
        final var expectedItemsCount = 0;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(aQuery)))
                .thenReturn(expectedPagination);
        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(categories.size(), actualResult.total());
    }

}

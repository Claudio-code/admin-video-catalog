package com.admin.catalog.infrastructure.category;

import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.infrastructure.MySQLGatewayTest;
import com.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMysqlGateway categoryMysqlGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
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
    public void givenAValidCategory_whenCallUpdate_shouldReturnCategoryUpdated() {
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
    public void givenInvalidCategoryId_whenTryDeleteIt_shouldDeleteCategory() {
        assertEquals(0, categoryRepository.count());
        categoryMysqlGateway.deletedById(CategoryID.from("INVALID"));
        assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeletedCategory() {
        final var aCategory = Category.newCategory("Movie", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        assertEquals(1, categoryRepository.count());

        categoryMysqlGateway.deletedById(aCategory.getId());

        assertEquals(0, categoryRepository.count());
    }

}

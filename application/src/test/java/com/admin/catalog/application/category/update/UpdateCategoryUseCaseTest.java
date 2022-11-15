package com.admin.catalog.application.category.update;

import com.admin.catalog.application.UseCaseTest;
import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    DefaultUpdateCategoryUseCase useCase;

    @Mock
    CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var categoryCreated = Category.newCategory("Movies22", null, true);
        final var expectedName = "Movies";
        final var expectedDescription = "The best movies";
        final var expectedActive = true;
        final var expectedId = categoryCreated.getId();
        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(Category.with(categoryCreated)));
        when(categoryGateway.update(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var test = new DefaultUpdateCategoryUseCase(categoryGateway);
        final var actualOutput = test.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).update(argThat(aUpdatedCategory ->
            Objects.equals(expectedName, aUpdatedCategory.getName())
                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                && Objects.equals(expectedActive, aUpdatedCategory.isActive())
                && Objects.equals(expectedId, aUpdatedCategory.getId())
                && Objects.equals(categoryCreated.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                && categoryCreated.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                && Objects.isNull(aUpdatedCategory.getDeletedAt())));
    }


    @Test
    void givenAInvalidName_whenCallUpdateCategory_thenShouldReturnDomainException() {
        final var categoryCreated = Category.newCategory("Movies22", null, true);
        final String expectedName = null;
        final var expectedDescription = "The best movies";
        final var expectedActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedId = categoryCreated.getId();
        final var aCommand = UpdateCategoryCommand
            .with(expectedId.getValue(), expectedName, expectedDescription, expectedActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(Category.with(categoryCreated)));
        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstOrError().message());
        verify(categoryGateway, times(0)).update(any());
    }


    @Test
    void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        final var categoryCreated = Category.newCategory("Movies22", null, true);
        final var expectedName = "Movies";
        final var expectedDescription = "The best movies";
        final var expectedActive = false;
        final var expectedId = categoryCreated.getId();
        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(Category.with(categoryCreated)));
        when(categoryGateway.update(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        assertTrue(categoryCreated.isActive());
        assertNull(categoryCreated.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).update(argThat(aUpdatedCategory ->
            Objects.equals(expectedName, aUpdatedCategory.getName())
                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                && Objects.equals(expectedActive, aUpdatedCategory.isActive())
                && Objects.equals(expectedId, aUpdatedCategory.getId())
                && Objects.equals(categoryCreated.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                && categoryCreated.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                && Objects.nonNull(aUpdatedCategory.getDeletedAt())));
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var categoryCreated = Category.newCategory("Movies22", null, true);
        final var expectedName = "Movies";
        final var expectedDescription = "The best movies";
        final var expectedActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;
        final var expectedId = categoryCreated.getId();
        final var aCommand = UpdateCategoryCommand
            .with(expectedId.getValue(), expectedName, expectedDescription, expectedActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(Category.with(categoryCreated)));
        when(categoryGateway.update(any())).thenThrow(new RuntimeException(expectedErrorMessage));

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstOrError().message());
        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).update(argThat(aUpdatedCategory ->
            Objects.equals(expectedName, aUpdatedCategory.getName())
                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                && Objects.equals(expectedActive, aUpdatedCategory.isActive())
                && Objects.equals(expectedId, aUpdatedCategory.getId())
                && Objects.equals(categoryCreated.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                && categoryCreated.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                && Objects.isNull(aUpdatedCategory.getDeletedAt())));
    }

    @Test
    void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Movies";
        final var expectedDescription = "The best movies";
        final var expectedActive = true;
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = "123";
        final var aCommand = UpdateCategoryCommand
            .with(expectedId, expectedName, expectedDescription, expectedActive);

        when(categoryGateway.findById(eq(CategoryID.from(expectedId)))).thenReturn(Optional.empty());
        final var actualException = assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        assertEquals(expectedErrorMessage, actualException.getMessage());
        verify(categoryGateway, times(1)).findById(eq(CategoryID.from(expectedId)));
        verify(categoryGateway, times(0)).update(any());
    }

}

package com.admin.catalog.application.category.update;

import com.admin.catalog.application.UseCaseTest;
import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        final var categoryCreated = Category.newCategory("Movies", null, true);
        final var expectedName = "Movies";
        final var expectedDescription = "The best movies";
        final var expectedActive = true;
        final var expectedId = categoryCreated.getId();
        final var aCommand = UpdateCategoryCommand
            .with(expectedId.getValue(), expectedName, expectedDescription, expectedActive);

        when(categoryGateway.findById(eq(expectedId)))
            .thenReturn(Optional.of(Category.with(categoryCreated)));

        when(categoryGateway.update(any()))
            .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var test = new DefaultUpdateCategoryUseCase(categoryGateway);
        final var actualOutput = test.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, Mockito.times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).update(argThat(aUpdatedCategory ->
            Objects.equals(expectedName, aUpdatedCategory.getName())
                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                && Objects.equals(expectedActive, aUpdatedCategory.isActive())
                && Objects.equals(expectedId, aUpdatedCategory.getId())
                && Objects.equals(categoryCreated.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                && categoryCreated.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                && Objects.isNull(aUpdatedCategory.getDeletedAt())));
    }




}

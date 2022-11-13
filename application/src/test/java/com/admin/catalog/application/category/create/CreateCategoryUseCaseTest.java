package com.admin.catalog.application.category.create;

import com.admin.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {

    @InjectMocks
    DefaultCreateCategoryUseCase useCase;

    @Mock
    CategoryGateway categoryGateway;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "The best movies";
        final var expectedActive = true;

        final var aCommand = CreateCategoryCommand
            .with(expectedName, expectedDescription, expectedActive);

        when(categoryGateway.create(any()))
            .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1))
            .create(argThat(aCategory -> Objects.equals(expectedName, aCategory.getName())
                && Objects.equals(expectedDescription, aCategory.getDescription())
                && Objects.equals(expectedActive, aCategory.isActive())
                && Objects.nonNull(aCategory.getCreatedAt())
                && Objects.isNull(aCategory.getDeletedAt())));
    }

    @Test
    void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "The best movies";
        final var expectedActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand
            .with(expectedName, expectedDescription, expectedActive);

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstOrError().message());

        verify(categoryGateway, times(0))
            .create(any());
    }

    @Test
    void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "The best movies";
        final var expectedActive = false;

        final var aCommand = CreateCategoryCommand
            .with(expectedName, expectedDescription, expectedActive);

        when(categoryGateway.create(any()))
            .thenAnswer(AdditionalAnswers.returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).create(argThat(aCategory ->
            Objects.equals(expectedName, aCategory.getName())
                && Objects.equals(expectedDescription, aCategory.getDescription())
                && Objects.equals(expectedActive, aCategory.isActive())
                && Objects.nonNull(aCategory.getCreatedAt())
                && Objects.nonNull(aCategory.getDeletedAt())));
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnException() {
        final var expectedName = "Movies";
        final var expectedDescription = "The best movies";
        final var expectedActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var aCommand = CreateCategoryCommand
            .with(expectedName, expectedDescription, expectedActive);

        when(categoryGateway.create(any()))
            .thenThrow(new RuntimeException(expectedErrorMessage));

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertEquals(expectedErrorMessage, notification.firstOrError().message());

        verify(categoryGateway, times(1))
            .create(argThat(aCategory -> Objects.equals(expectedName, aCategory.getName())
                && Objects.equals(expectedDescription, aCategory.getDescription())
                && Objects.equals(expectedActive, aCategory.isActive())
                && Objects.nonNull(aCategory.getCreatedAt())
                && Objects.isNull(aCategory.getDeletedAt())));
    }

}

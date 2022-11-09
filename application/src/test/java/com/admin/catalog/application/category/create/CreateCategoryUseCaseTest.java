package com.admin.catalog.application.category.create;

import org.junit.jupiter.api.Test;

class CreateCategoryUseCaseTest {

    @Test
    void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "The best movies";
        final var expectedActive = true;

         final var aCommand = CreateCategoryCommand
             .with(expectedName, expectedDescription, expectedActive);

    }

}

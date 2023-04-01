package com.admin.catalog.application.genre.create;

import com.admin.catalog.application.UseCaseTest;
import com.admin.catalog.application.category.validate.ValidateCategoriesUseCase;
import com.admin.catalog.domain.genre.GenreGateway;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

public class CreateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    DefaultCreateGenreUseCase useCase;

    @Mock
    GenreGateway genreGateway;

    @Mock
    ValidateCategoriesUseCase validateCategoriesUseCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway, validateCategoriesUseCase);
    }

}

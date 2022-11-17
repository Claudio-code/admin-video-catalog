package com.admin.catalog.application.category.retrieve.get;

import com.admin.catalog.application.UseCase;
import com.admin.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

class GetCategoryByIdUseCaseTest extends UseCase {

    @InjectMocks
    DefaultGetCategoryByIdUseCase useCase;

    @Mock
    CategoryGateway categoryGateway;

    @Override
    public Object execute(Object aIn) {
        return List.of(categoryGateway);
    }


    @Test
    void given() {

    }

}

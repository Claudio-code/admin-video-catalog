package com.admin.catalog.infrastructure.configuration.usecases;

import com.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.admin.catalog.application.category.create.DefaultCreateCategoryUseCase;
import com.admin.catalog.application.category.delete.DefaultDeleteCategoryUseCase;
import com.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.admin.catalog.application.category.retrieve.list.DefaultListCategoryUseCase;
import com.admin.catalog.application.category.retrieve.list.ListCategoryUseCase;
import com.admin.catalog.application.category.update.DefaultUpdateCategoryUseCase;
import com.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.admin.catalog.domain.category.CategoryGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CategoryUseCaseConfig {

    private final CategoryGateway categoryGateway;

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }

    @Bean
    public ListCategoryUseCase listCategoryUseCase() {
        return new DefaultListCategoryUseCase(categoryGateway);
    }

}

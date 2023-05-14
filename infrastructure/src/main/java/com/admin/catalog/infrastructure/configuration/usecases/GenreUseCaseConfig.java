package com.admin.catalog.infrastructure.configuration.usecases;

import com.admin.catalog.application.category.validate.ValidateCategoriesUseCase;
import com.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.admin.catalog.application.genre.create.DefaultCreateGenreUseCase;
import com.admin.catalog.application.genre.delete.DefaultDeleteGenreUseCase;
import com.admin.catalog.application.genre.delete.DeleteGenreUseCase;
import com.admin.catalog.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import com.admin.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.admin.catalog.application.genre.retrieve.list.DefaultListGenreUseCase;
import com.admin.catalog.application.genre.retrieve.list.ListGenreUseCase;
import com.admin.catalog.application.genre.update.DefaultUpdateGenreUseCase;
import com.admin.catalog.application.genre.update.UpdateGenreUseCase;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.genre.GenreGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GenreUseCaseConfig {

    private final GenreGateway genreGateway;

    @Bean
    public CreateGenreUseCase createGenreUseCase(CategoryGateway categoryGateway) {
        final var validate = new ValidateCategoriesUseCase(categoryGateway);
        return new DefaultCreateGenreUseCase(validate, genreGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase(CategoryGateway categoryGateway) {
        final var validate = new ValidateCategoriesUseCase(categoryGateway);
        return new DefaultUpdateGenreUseCase(validate, genreGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DefaultDeleteGenreUseCase(genreGateway);
    }

    @Bean
    public ListGenreUseCase listGenreUseCase() {
        return new DefaultListGenreUseCase(genreGateway);
    }

    @Bean
    public GetGenreByIdUseCase getGenreByIdUseCase() {
        return new DefaultGetGenreByIdUseCase(genreGateway);
    }

}

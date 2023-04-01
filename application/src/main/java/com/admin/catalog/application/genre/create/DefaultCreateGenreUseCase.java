package com.admin.catalog.application.genre.create;

import com.admin.catalog.application.category.validate.ValidateCategoriesUseCase;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.exceptions.NotificationException;
import com.admin.catalog.domain.genre.Genre;
import com.admin.catalog.domain.genre.GenreGateway;
import com.admin.catalog.domain.validation.handler.Notification;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultCreateGenreUseCase extends CreateGenreUseCase {

    private static final String CREATE_GENRE_ERROR = "Could not create aggregate Genre";
    private final ValidateCategoriesUseCase validateCategories;
    private final GenreGateway genreGateway;

    @Override
    public CreateGenreOutput execute(CreateGenreCommand aIn) {
        final var aName = aIn.name();
        final var isActive = aIn.isActive();
        final var categories = CategoryID.from(aIn.categories());
        final var notification = Notification.create();

        notification.append(validateCategories.validateCategories(categories));
        final var aGenre = notification.validate(() -> Genre.newGenre(aName, isActive));
        if (notification.hasError()) {
            throw new NotificationException(CREATE_GENRE_ERROR, notification);
        }

        aGenre.addCategories(categories);
        return CreateGenreOutput.from(genreGateway.create(aGenre));
    }

}

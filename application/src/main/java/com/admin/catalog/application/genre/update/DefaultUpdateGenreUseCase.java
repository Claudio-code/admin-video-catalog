package com.admin.catalog.application.genre.update;

import com.admin.catalog.application.category.validate.ValidateCategoriesUseCase;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.exceptions.NotFoundException;
import com.admin.catalog.domain.exceptions.NotificationException;
import com.admin.catalog.domain.genre.Genre;
import com.admin.catalog.domain.genre.GenreGateway;
import com.admin.catalog.domain.genre.GenreID;
import com.admin.catalog.domain.validation.handler.Notification;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase {

    private static final String UPDATE_GENRE_ERROR = "Could not update aggregate Genre %s";
    private final ValidateCategoriesUseCase validateCategories;
    private final GenreGateway genreGateway;

    @Override
    public UpdateGenreOutput execute(UpdateGenreCommand aIn) {
        final var anId = GenreID.from(aIn.id());
        final var aName = aIn.name();
        final var isActive = aIn.isActive();
        final var categories = CategoryID.from(aIn.categories());

        final var aGenre = genreGateway.findById(anId)
            .orElseThrow(NotFoundException.notFoundSupplier(anId, Genre.class));
        final var notification = Notification.create();
        notification.append(validateCategories.validateCategories(categories));
        notification.validate(() -> aGenre.update(aName, isActive, categories));

        if (notification.hasError()) {
            throw new NotificationException(UPDATE_GENRE_ERROR.formatted(aIn.id()), notification);
        }

        return UpdateGenreOutput.from(genreGateway.update(aGenre));
    }

}

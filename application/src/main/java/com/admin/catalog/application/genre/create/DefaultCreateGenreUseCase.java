package com.admin.catalog.application.genre.create;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.exceptions.NotificationException;
import com.admin.catalog.domain.genre.Genre;
import com.admin.catalog.domain.genre.GenreGateway;
import com.admin.catalog.domain.validation.Error;
import com.admin.catalog.domain.validation.ValidationHandler;
import com.admin.catalog.domain.validation.handler.Notification;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultCreateGenreUseCase extends CreateGenreUseCase {

    private static final String CATEGORY_IDS_ERROR_MESSAGE = "Some categories could not be found: %s";
    private static final String CREATE_GENRE_ERROR = "Could not create aggregate Genre";
    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    @Override
    public CreateGenreOutput execute(CreateGenreCommand aIn) {
        final var aName = aIn.name();
        final var isActive = aIn.isActive();
        final var categories = toCategoryID(aIn.categories());
        final var notification = Notification.create();

        notification.append(validateCategories(categories));
        final var aGenre = notification.validate(() -> Genre.newGenre(aName, isActive));
        if (notification.hasError()) {
            throw new NotificationException(CREATE_GENRE_ERROR, notification);
        }

        aGenre.addCategories(categories);
        return CreateGenreOutput.from(genreGateway.create(aGenre));
    }

    private ValidationHandler validateCategories(final List<CategoryID> ids) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = categoryGateway.existsByIds(ids);
        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                .map(CategoryID::getValue)
                .collect(Collectors.joining(", "));

            notification.append(new Error(CATEGORY_IDS_ERROR_MESSAGE.formatted(missingIdsMessage)));
        }
        return notification;
    }

    private List<CategoryID> toCategoryID(final List<String> categories) {
        return categories.stream()
            .map(CategoryID::from)
            .toList();
    }

}

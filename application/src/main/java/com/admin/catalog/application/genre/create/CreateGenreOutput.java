package com.admin.catalog.application.genre.create;

import com.admin.catalog.domain.genre.Genre;

public record CreateGenreOutput(String id) {

    public static CreateGenreOutput from(final String aId) {
        return new CreateGenreOutput(aId);
    }

    public static CreateGenreOutput from(final Genre genre) {
        return new CreateGenreOutput(genre.getId().getValue());
    }

}

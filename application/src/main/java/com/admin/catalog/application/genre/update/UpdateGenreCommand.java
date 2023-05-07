package com.admin.catalog.application.genre.update;

import java.util.List;
import java.util.Objects;

public record UpdateGenreCommand(
    String id,
    String name,
    boolean isActive,
    List<String> categories
) {

    public static UpdateGenreCommand with(
        final String id,
        final String name,
        final Boolean isActive,
        final List<String> categories
    ) {
        final boolean active = Objects.requireNonNullElse(isActive, true);
        return new UpdateGenreCommand(id, name, active, categories);
    }

}

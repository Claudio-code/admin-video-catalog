package com.admin.catalog.application.genre.retrieve.get;

import java.time.Instant;
import java.util.List;

import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.genre.Genre;

public record GenreOutput(
        String id,
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    public static GenreOutput from(final Genre genre) {
        final var categories = genre.getCategories().stream()
            .map(CategoryID::getValue)
            .toList();
        return new GenreOutput(
            genre.getId().getValue(),
            genre.getName(),
            genre.isActive(),
            categories,
            genre.getCreatedAt(),
            genre.getUpdatedAt(),
            genre.getDeletedAt()
        );
    }

}

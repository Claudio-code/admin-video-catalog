package com.admin.catalog.application.genre.retrieve.list;

import com.admin.catalog.domain.genre.Genre;

import java.time.Instant;

public record GenreListOutput(
    String id,
    String name,
    boolean isActive,
    Instant createdAt,
    Instant deletedAt
) {

    public static GenreListOutput from(final Genre genre) {
        return new GenreListOutput(
            genre.getId().getValue(),
            genre.getName(),
            genre.isActive(),
            genre.getCreatedAt(),
            genre.getDeletedAt()
        );
    }

}

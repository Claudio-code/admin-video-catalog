package com.admin.catalog.domain.category;

import java.time.Instant;
import java.util.UUID;

public class Category {
    private final String id;
    private final String name;
    private final String description;
    private final boolean isActive;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public Category(final String id,
                    final String name,
                    final String description,
                    final boolean isActive,
                    final Instant createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public static Category newCategory(final String name,
                                       final String description,
                                       final boolean isActive) {
        final var uuid = UUID.randomUUID().toString();
        return new Category(uuid, name, description, isActive, Instant.now());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return isActive;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}

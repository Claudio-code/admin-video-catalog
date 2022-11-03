package com.admin.catalog.domain.category;

import com.admin.catalog.domain.AggregateRoot;
import com.admin.catalog.domain.validation.ValidationHandler;

import java.time.Instant;

public class Category extends AggregateRoot<CategoryID> {

    private String name;
    private String description;
    private boolean isActive;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(final CategoryID id,
                    final String name,
                    final String description,
                    final boolean isActive,
                    final Instant createdAt) {
        super(id);
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public static Category newCategory(final String name,
                                       final String description,
                                       final boolean isActive) {
        final var uuid = CategoryID.unique();
        return new Category(uuid, name, description, isActive, Instant.now());
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

    public void update(final String aName, final String aDescription, final boolean aIsActive) {
        name = aName;
        description = aDescription;
        if (aIsActive) {
            active();
            return;
        }
        deactivate();
    }

    public void deactivate() {
        if (deletedAt == null) {
            deletedAt = Instant.now();
        }
        isActive = false;
        updatedAt = Instant.now();
    }

    public void active() {
        deletedAt = null;
        isActive = true;
        updatedAt = Instant.now();
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

}

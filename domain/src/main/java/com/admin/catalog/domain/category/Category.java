package com.admin.catalog.domain.category;

import com.admin.catalog.domain.AggregateRoot;
import com.admin.catalog.domain.validation.ValidationHandler;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Category extends AggregateRoot<CategoryID> {

    private String name;
    private String description;
    private boolean isActive;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(final CategoryID id, final String name, final String description, final boolean isActive, final Instant createdAt) {
        super(id);
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        changeStatus(isActive);
    }

    public static Category newCategory(final String name, final String description, final boolean isActive) {
        final var uuid = CategoryID.unique();
        return new Category(uuid, name, description, isActive, Instant.now());
    }

    public static Category with(final CategoryID id,
                                final String name,
                                final String description,
                                final boolean isActive,
                                final Instant createdAt) {
        return new Category(id, name, description, isActive, createdAt);
    }

    public static Category with(final Category category) {
        return with(
            category.getId(),
            category.getName(),
            category.getDescription(),
            category.isActive(),
            category.getCreatedAt()
        );
    }

    public Category update(final String aName, final String aDescription, final boolean aIsActive) {
        name = aName;
        description = aDescription;
        changeStatus(aIsActive);
        return this;
    }

    private void changeStatus(final boolean aIsActive) {
        this.isActive = aIsActive;
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

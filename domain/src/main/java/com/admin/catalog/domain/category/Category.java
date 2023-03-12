package com.admin.catalog.domain.category;

import com.admin.catalog.domain.AggregateRoot;
import com.admin.catalog.domain.util.InstantUtils;
import com.admin.catalog.domain.validation.ValidationHandler;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Category extends AggregateRoot<CategoryID> implements Cloneable {

    private String name;
    private String description;
    private boolean isActive;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
        final CategoryID id,
        final String name,
        final String description,
        final boolean isActive,
        final Instant createdAt
    ) {
        super(id);
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        changeStatus(isActive);
    }

    private Category(
        final CategoryID categoryID,
        final String name,
        final String description,
        final boolean isActive,
        final Instant createdAt,
        final Instant updatedAt,
        final Instant deletedAt
    ) {
        super(categoryID);
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Category newCategory(final String name, final String description, final boolean isActive) {
        final var uuid = CategoryID.unique();
        return new Category(uuid, name, description, isActive, InstantUtils.now());
    }

    public static Category with(
        final CategoryID id,
        final String name,
        final String description,
        final boolean isActive,
        final Instant createdAt
    ) {
        return new Category(id, name, description, isActive, createdAt);
    }

    public static Category with(
        final CategoryID id,
        final String name,
        final String description,
        final boolean isActive,
        final Instant createdAt,
        final Instant updatedAt,
        final Instant deletedAt
    ) {
        return new Category(id, name, description, isActive, createdAt, updatedAt, deletedAt);
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

    public Category update(
        final String aName,
        final String aDescription,
        final boolean aIsActive
    ) {
        name = aName;
        description = aDescription;
        changeStatus(aIsActive);
        return this;
    }

    private void changeStatus(final boolean aIsActive) {
        if (aIsActive) {
            active();
            return;
        }
        deactivate();
    }

    public void deactivate() {
        if (deletedAt == null) {
            deletedAt = InstantUtils.now();
        }
        isActive = false;
        updatedAt = InstantUtils.now();
    }

    public void active() {
        deletedAt = null;
        isActive = true;
        updatedAt = InstantUtils.now();
    }

    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}

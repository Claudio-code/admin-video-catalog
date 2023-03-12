package com.admin.catalog.domain.genre;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.admin.catalog.domain.AggregateRoot;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.util.InstantUtils;
import lombok.Getter;

@Getter
public class Genre extends AggregateRoot<GenreID> implements Cloneable {

    private final String name;
    private boolean isActive;
    private final List<CategoryID> categories;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Genre(
        final GenreID id,
        final String name,
        final boolean isActive,
        final List<CategoryID> categories,
        final Instant createdAt
    ) {
        super(id);
        this.name = name;
        this.isActive = isActive;
        this.categories = categories;
        this.createdAt = createdAt;
        changeStatus(isActive);
    }

    public Genre(
        final GenreID id,
        final String name,
        final boolean isActive,
        final List<CategoryID> categories,
        final Instant createdAt,
        final Instant updatedAt,
        final Instant deletedAt
    ) {
        super(id);
        this.name = name;
        this.isActive = isActive;
        this.categories = categories;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Genre newGenre(final String aName, final boolean isActive) {
        final var anId = GenreID.unique();
        final var now = InstantUtils.now();
        return new Genre(anId, aName, isActive, new ArrayList<>(), now);
    }

    public static Genre with(final Genre genre) {
        return new Genre(
            genre.id,
            genre.name,
            genre.isActive,
            new ArrayList<>(genre.categories),
            genre.createdAt,
            genre.updatedAt,
            genre.deletedAt
        );
    }

    public static Genre with(
        final GenreID id,
        final String name,
        final boolean isActive,
        final List<CategoryID> categories,
        final Instant createdAt,
        final Instant updatedAt,
        final Instant deletedAt
    ) {
        return new Genre(id, name, isActive, categories, createdAt, updatedAt, deletedAt);
    }

    @Override
    public Genre clone() {
        try {
            return (Genre) super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public Genre addCategory(final CategoryID aCategoryID) {
        if (aCategoryID == null) {
            return this;
        }
        categories.add(aCategoryID);
        updatedAt = InstantUtils.now();
        return this;
    }

    public Genre addCategories(final List<CategoryID> aCategories) {
        if (aCategories == null || aCategories.isEmpty()) {
            return this;
        }
        categories.addAll(aCategories);
        updatedAt = InstantUtils.now();
        return this;
    }

    public Genre removeCategory(final CategoryID aCategoryID) {
        if (aCategoryID == null) {
            return this;
        }
        categories.remove(aCategoryID);
        updatedAt = InstantUtils.now();
        return this;
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

    private void changeStatus(final boolean aIsActive) {
        if (aIsActive) {
            active();
            return;
        }
        deactivate();
    }

}

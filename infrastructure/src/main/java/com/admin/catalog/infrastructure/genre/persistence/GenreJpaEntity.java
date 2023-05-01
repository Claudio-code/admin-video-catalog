package com.admin.catalog.infrastructure.genre.persistence;

import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.genre.Genre;
import com.admin.catalog.domain.genre.GenreID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Genre")
@Table(name = "genres")
public class GenreJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "active", nullable = false)
    private boolean active;
    @OneToMany(mappedBy = "genre", cascade = ALL, fetch = EAGER, orphanRemoval = true)
    private Set<GenreCategoryJpaEntity> categories;
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;
    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;


    public static GenreJpaEntity from(final Genre aGenre) {
        final var anEntity = new GenreJpaEntity(
            aGenre.getId().getValue(),
            aGenre.getName(),
            aGenre.isActive(),
            new HashSet<>(),
            aGenre.getCreatedAt(),
            aGenre.getUpdatedAt(),
            aGenre.getDeletedAt()
        );

        aGenre.getCategories()
            .forEach(anEntity::addCategory);
        return anEntity;
    }

    public Genre toAggregate() {
        return Genre.with(
            GenreID.from(id),
            name,
            active,
            getCategories(),
            createdAt,
            updatedAt,
            deletedAt
        );
    }

    private void addCategory(final CategoryID id) {
        categories.add(GenreCategoryJpaEntity.from(this, id));
    }


    public List<CategoryID> getCategories() {
        return categories.stream()
            .map(it -> CategoryID.from(it.getId().getCategoryId()))
            .toList();
    }

}

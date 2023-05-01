package com.admin.catalog.infrastructure.genre;

import com.admin.catalog.domain.genre.Genre;
import com.admin.catalog.domain.genre.GenreGateway;
import com.admin.catalog.domain.genre.GenreID;
import com.admin.catalog.domain.pagination.Pagination;
import com.admin.catalog.domain.pagination.SearchQuery;
import com.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import com.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import com.admin.catalog.infrastructure.util.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Component
public class GenreMySQLGateway implements GenreGateway {

    private final GenreRepository genreRepository;

    @Override
    public Genre create(final Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public void deleteById(final GenreID anId) {
        if (!genreRepository.existsById(anId.getValue())) {
            return;
        }
        genreRepository.deleteById(anId.getValue());
    }

    @Override
    public Optional<Genre> findById(final GenreID anId) {
        return genreRepository.findById(anId.getValue())
            .map(GenreJpaEntity::toAggregate);
    }

    @Override
    public Genre update(final Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public Pagination<Genre> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
            aQuery.page(),
            aQuery.perPage(),
            Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );
        final var where = Optional.ofNullable(aQuery.terms())
            .filter(str -> !str.isBlank())
            .map(this::assembleSpecification)
            .orElse(null);
        final var pageResult = genreRepository.findAll(Specification.where(where), page);
        return new Pagination<>(
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.getTotalElements(),
            pageResult.map(GenreJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<GenreID> existsByIds(final Iterable<GenreID> genreIDS) {
        final var ids = StreamSupport.stream(genreIDS.spliterator(), false)
            .map(GenreID::getValue)
            .toList();
        return genreRepository.existsByIds(ids).stream()
            .map(GenreID::from)
            .toList();
    }

    private Genre save(final Genre aGenre) {
        return genreRepository.save(GenreJpaEntity.from(aGenre))
            .toAggregate();
    }

    private Specification<GenreJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }

}

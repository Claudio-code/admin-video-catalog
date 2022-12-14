package com.admin.catalog.infrastructure.category;

import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.exceptions.ValidatorException;
import com.admin.catalog.domain.pagination.Pagination;
import com.admin.catalog.domain.pagination.SearchQuery;
import com.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.admin.catalog.infrastructure.category.persistence.CategoryModelValidator;
import com.admin.catalog.infrastructure.category.persistence.CategoryRepository;
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
public class CategoryMysqlGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;
    private final CategoryModelValidator validator;

    @Override
    public Category create(final Category aCategory) throws ValidatorException {
        return save(aCategory);
    }

    @Override
    public void deletedById(final CategoryID anId) {
        final var anIdValue = anId.getValue();
        if (!categoryRepository.existsById(anIdValue)) {
            return;
        }
        categoryRepository.deleteById(anIdValue);
    }

    @Override
    public Optional<Category> findById(final CategoryID anId) {
        return categoryRepository.findById(anId.getValue())
            .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(final Category aCategory) throws ValidatorException {
        return save(aCategory);
    }

    @Override
    public Pagination<Category> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
            aQuery.page(),
            aQuery.perPage(),
            Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var specifications = Optional.ofNullable(aQuery.terms())
            .filter(str -> !str.isBlank())
            .map(this::assempleSpecification)
            .map(Specification::where)
            .orElse(null);

        final var pageResult = categoryRepository.findAll(specifications, page);

        return new Pagination<>(
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.getTotalElements(),
            pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<CategoryID> existsByIds(final Iterable<CategoryID> categoryIds) {
        final var ids = StreamSupport.stream(categoryIds.spliterator(), false)
            .map(CategoryID::getValue)
            .toList();
        return categoryRepository.existsByIds(ids)
            .stream()
            .map(CategoryID::from)
            .toList();
    }

    private Category save(final Category aCategory) throws ValidatorException {
        final var categoryToSave = CategoryJpaEntity.from(aCategory);
        validator.validate(categoryToSave, CategoryJpaEntity.class);
        return categoryRepository.save(categoryToSave).toAggregate();
    }

    private Specification<CategoryJpaEntity> assempleSpecification(final String str) {
        final Specification<CategoryJpaEntity> nameLike = SpecificationUtils.like("name", str);
        final Specification<CategoryJpaEntity> descriptionLike = SpecificationUtils.like("description", str);
        return nameLike.or(descriptionLike);
    }

}

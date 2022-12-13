package com.admin.catalog.infrastructure.util;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class SpecificationUtils {

    public static <T> Specification<T> like(final String prop, final String term) {
        return (root, query, criteriaBuilder) -> criteriaBuilder
            .like(criteriaBuilder.upper(root.get(prop)), SqlUtil.like(term.toUpperCase()));
    }

}

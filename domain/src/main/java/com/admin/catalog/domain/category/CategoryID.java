package com.admin.catalog.domain.category;

import java.util.List;
import java.util.Objects;

import com.admin.catalog.domain.Identifier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CategoryID extends Identifier {

    private final String value;

    public static CategoryID unique() {
        return CategoryID.from(uuid());
    }

    public static CategoryID from(final String anId) {
        return new CategoryID(anId);
    }

    public static List<CategoryID> from(final List<String> categories) {
        return categories.stream()
            .map(CategoryID::from)
            .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CategoryID that = (CategoryID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

}

package com.admin.catalog.domain.genre;

import java.util.Objects;

import com.admin.catalog.domain.Identifier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GenreID extends Identifier {

    private final String value;

    public static GenreID unique() {
        return GenreID.from(uuid());
    }

    public static GenreID from(final String anId) {
        return new GenreID(anId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenreID that = (GenreID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

}

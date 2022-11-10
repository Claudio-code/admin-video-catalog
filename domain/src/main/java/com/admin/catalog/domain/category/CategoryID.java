package com.admin.catalog.domain.category;

import com.admin.catalog.domain.Identifier;

import java.util.UUID;

public class CategoryID extends Identifier {

    private final String value;

    private CategoryID(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static CategoryID unique() {
        final var uuid = UUID.randomUUID();
        return from(uuid);
    }

    @Override
    public String getValue() {
        return value;
    }

    public static CategoryID from(final String anId) {
        return new CategoryID(anId.toLowerCase());
    }

    public static CategoryID from(final UUID anID) {
        return from(anID.toString());
    }

}

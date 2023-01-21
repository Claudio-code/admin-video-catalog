package com.admin.catalog.application.category.update;

public record UpdateCategoryCommand(String id, String name, String description, boolean isActive) {

    public static UpdateCategoryCommand with(final String anId,
                                             final String aName,
                                             final String aDescription,
                                             final Boolean aIsActive) {
        return new UpdateCategoryCommand(anId, aName, aDescription, aIsActive != null ? aIsActive : true);
    }

}

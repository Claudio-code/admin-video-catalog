package com.admin.catalog.application.category.create;

public record CreateCategoryCommand(String name, String description, boolean isActive) {

    public static CreateCategoryCommand with(final String aName,
                                             final String aDescription,
                                             final Boolean aIsActive) {
        return new CreateCategoryCommand(aName, aDescription, aIsActive != null ? aIsActive : true);
    }

}

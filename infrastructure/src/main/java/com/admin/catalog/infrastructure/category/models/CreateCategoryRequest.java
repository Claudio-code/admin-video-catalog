package com.admin.catalog.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import javax.validation.constraints.NotBlank;

@Builder
public record CreateCategoryRequest(
    @NotBlank
    @JsonProperty("name")
    String name,
    @NotBlank
    @JsonProperty("description")
    String description,
    @NotBlank
    @JsonProperty("is_active")
    Boolean active
) {
}

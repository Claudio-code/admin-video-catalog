package com.admin.catalog.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CreateCategoryRequest(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("description") String description,
    @JsonProperty("is_active") Boolean active
) {
}

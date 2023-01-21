package com.admin.catalog.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record CategoryListResponse(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("description") String description,
    @JsonProperty("is_active") Boolean active,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("deleted_at") Instant deletedAt
) {
}

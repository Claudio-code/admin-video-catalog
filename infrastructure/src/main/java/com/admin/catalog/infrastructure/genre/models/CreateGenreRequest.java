package com.admin.catalog.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;

public record CreateGenreRequest(
    @NotBlank
    @JsonProperty("name") String name,
    @JsonProperty("categories_id") List<String> categories,
    @JsonProperty("is_active") Boolean active
) {

    public boolean isActive() {
        if (active == null) {
            return true;
        }
        return active;
    }

    public List<String> categories() {
        return categories != null ? categories : Collections.emptyList();
    }

}

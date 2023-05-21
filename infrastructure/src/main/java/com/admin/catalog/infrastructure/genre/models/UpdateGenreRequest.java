package com.admin.catalog.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;

public record UpdateGenreRequest(
    @NotBlank
    @JsonProperty("name") String name,
    @JsonProperty("categories_id") List<String> categories,
    @JsonProperty("is_active") Boolean active
) {

    public List<String> categories() {
        return categories != null ? categories : Collections.emptyList();
    }

    public boolean isActive() {
        if (active == null) {
            return true;
        }
        return active;
    }

}

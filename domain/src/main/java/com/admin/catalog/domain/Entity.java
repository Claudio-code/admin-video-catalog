package com.admin.catalog.domain;

import com.admin.catalog.domain.validation.ValidationHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public abstract class Entity<ID extends Identifier> {

    protected final ID id;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Entity<?> entity = (Entity<?>) object;
        return getId().equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public abstract void validate(ValidationHandler handler);

}

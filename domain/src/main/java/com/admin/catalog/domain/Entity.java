package com.admin.catalog.domain;

import com.admin.catalog.domain.events.DomainEvent;
import com.admin.catalog.domain.events.DomainEventPublisher;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class Entity<ID extends Identifier> {

    protected final ID id;
    @Getter(AccessLevel.NONE)
    private final List<DomainEvent> domainEvents;

    protected Entity(ID id) {
        this(id, null);
    }

    protected Entity(ID id, List<DomainEvent> domainEvents) {
        this.id = id;
        this.domainEvents = new ArrayList<>(domainEvents == null ? Collections.emptyList() : domainEvents);
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void publishDomainEvents(final DomainEventPublisher publisher) {
        if (publisher == null) {
            return;
        }
        getDomainEvents()
            .forEach(publisher::publishEvent);
        domainEvents.clear();
    }

    public void registerEvent(final DomainEvent event) {
        if (event == null) {
            return;
        }
        domainEvents.add(event);
    }

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

}

package com.admin.catalog.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.admin.catalog.domain.events.DomainEvent;
import com.admin.catalog.domain.util.IdUtils;
import com.admin.catalog.domain.util.InstantUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityTest extends UnitTest {

    @Test
    void givenNullAsEvents_whenInstantiable_shouldBoOk() {
        final var anEntity = new DummyEntity(new DummyID(), null);

        assertNotNull(anEntity.getDomainEvents());
        assertTrue(anEntity.getDomainEvents().isEmpty());
    }

    @Test
    void givenDomainEvents_whenPassInConstructor_shouldCreateADefensiveClone() {
        final var expectEvents = 1;
        final List<DomainEvent> events = new ArrayList<>();
        events.add(new DummyEvent());

        final var anEntity = new DummyEntity(new DummyID(), events);

        assertNotNull(anEntity.getDomainEvents());
        assertEquals(expectEvents, anEntity.getDomainEvents().size());
        assertThrows(RuntimeException.class, () -> {
           final var actualEvents = anEntity.getDomainEvents();
           actualEvents.add(new DummyEvent());
        });
    }

    @Test
    void givenEmptyDomainEvents_whenCallsRegisterEvent_shouldAddEventToList() {
        final var expectEvents = 1;
        final var anEntity = new DummyEntity(new DummyID(), new ArrayList<>());
        anEntity.registerEvent(new DummyEvent());

        assertNotNull(anEntity.getDomainEvents());
        assertEquals(expectEvents, anEntity.getDomainEvents().size());
    }

    @Test
    void givenAFewDomainEvents_whenCallsPublishEvents_shouldCallPublisherClearTheList() {
        final var expectEvents = 0;
        final var expectSentEvents = 2;
        final var counter = new AtomicInteger(0);
        final var anEntity = new DummyEntity(new DummyID(), new ArrayList<>());

        anEntity.registerEvent(new DummyEvent());
        anEntity.registerEvent(new DummyEvent());

        assertEquals(2, anEntity.getDomainEvents().size());

        anEntity.publishDomainEvents(event -> counter.incrementAndGet());

        assertNotNull(anEntity.getDomainEvents());
        assertEquals(expectEvents, anEntity.getDomainEvents().size());
        assertEquals(expectSentEvents, counter.get());
    }

    public static class DummyEvent implements DomainEvent {

        @Override
        public Instant occurredOn() {
            return InstantUtils.now();
        }

    }

    public static class DummyID extends Identifier {
        private final String id;

        public DummyID() {
            id = IdUtils.uuid();
        }

        @Override
        public String getValue() {
            return id;
        }

    }

    public static class DummyEntity extends Entity<DummyID> {

        public DummyEntity(DummyID dummyID, List<DomainEvent> domainEvents) {
            super(dummyID, domainEvents);
        }

    }

}

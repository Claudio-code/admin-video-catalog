package com.admin.catalog.domain.castmember;

import com.admin.catalog.domain.AggregateRoot;
import lombok.Getter;

import java.time.Instant;

@Getter
public class CastMember extends AggregateRoot<CastMemberID> {

    private String name;
    private CastMemberType type;
    private final Instant createdAt;
    private Instant updatedAt;

    protected CastMember(
        final CastMemberID anId,
        final String aName,
        final CastMemberType aType,
        final Instant aCreationDate,
        final Instant aUpdateDate
    ) {
        super(anId);
        this.name = aName;
        this.type = aType;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
    }

    public static CastMember newMember(
        final String aName,
        final CastMemberType aType
    ) {
        final var anId = CastMemberID.unique();
        final var now = Instant.now();
        return new CastMember(anId, aName, aType, now, now);
    }

    public static CastMember with(final CastMember aMember) {
        return new CastMember(
            aMember.id,
            aMember.name,
            aMember.type,
            aMember.createdAt,
            aMember.updatedAt
        );
    }

    public CastMember update(final String aName, final CastMemberType aType) {
        this.name = aName;
        this.type = aType;
        this.updatedAt = Instant.now();
        return this;
    }
}

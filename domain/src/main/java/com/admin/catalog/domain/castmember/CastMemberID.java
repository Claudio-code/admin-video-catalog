package com.admin.catalog.domain.castmember;

import com.admin.catalog.domain.Identifier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CastMemberID extends Identifier {

    private final String value;

    public static CastMemberID unique() {
        return CastMemberID.from(uuid());
    }

    public static CastMemberID from(final String anId) {
        return new CastMemberID(anId);
    }

    public static List<CastMemberID> from(final List<String> castmembers) {
        return castmembers.stream()
            .map(CastMemberID::from)
            .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CastMemberID that = (CastMemberID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public String getValue() {
        return value;
    }
}

package com.admin.catalog.domain.castmember;

import com.admin.catalog.domain.pagination.Pagination;
import com.admin.catalog.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CastMemberGateway {
    CastMember create(final CastMember aCastMember);

    void deleteById(final CastMemberID anId);

    Optional<CastMember> findById(final CastMemberID anId);

    CastMember update(final CastMember aCastMember);

    Pagination<CastMember> findAll(SearchQuery query);

    List<CastMemberID> existsById(final Iterable<CastMemberID> ids);
}

package com.admin.catalog.application.genre.retrieve.get;

import com.admin.catalog.domain.exceptions.NotFoundException;
import com.admin.catalog.domain.genre.Genre;
import com.admin.catalog.domain.genre.GenreGateway;
import com.admin.catalog.domain.genre.GenreID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultGetGenreByUseCase extends GetGenreByUseCase {

    private final GenreGateway genreGateway;

    @Override
    public GenreOutput execute(String aIn) {
        final var genreId = GenreID.from(aIn);
        return genreGateway.findById(genreId)
            .map(GenreOutput::from)
            .orElseThrow(() -> NotFoundException.with(Genre.class.getSimpleName(), genreId));
    }

}

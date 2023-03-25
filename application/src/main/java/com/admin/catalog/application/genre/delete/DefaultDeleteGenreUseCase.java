package com.admin.catalog.application.genre.delete;

import com.admin.catalog.domain.genre.GenreGateway;
import com.admin.catalog.domain.genre.GenreID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase {

    private final GenreGateway genreGateway;

    @Override
    public void execute(String aIn) {
        genreGateway.deleteById(GenreID.from(aIn));
    }

}

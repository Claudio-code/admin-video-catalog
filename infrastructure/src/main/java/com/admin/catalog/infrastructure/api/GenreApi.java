package com.admin.catalog.infrastructure.api;

import com.admin.catalog.domain.pagination.Pagination;
import com.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.admin.catalog.infrastructure.genre.models.GenreListResponse;
import com.admin.catalog.infrastructure.genre.models.GenreResponse;
import com.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "genres")
@Tag(name = "Genre")
public interface GenreApi {

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new genre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> create(@Valid @RequestBody final CreateGenreRequest input);

    @GetMapping
    @Operation(summary = "List all genre paginated")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listed successfully"),
        @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    Pagination<GenreListResponse> list(
        @RequestParam(name = "search", required = false, defaultValue = "") final String search,
        @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
        @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
        @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
        @RequestParam(name = "direction", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a genre by it identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Genre retrieved successfully"),
        @ApiResponse(responseCode = "422", description = "Genre was not found"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    GenreResponse getById(@PathVariable(name = "id") final String id);

    @PutMapping(value = "{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a genre by it identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Genre updated successfully"),
        @ApiResponse(responseCode = "422", description = "Genre was not found"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> updatedById(
        @PathVariable(name = "id") final String id,
        @Valid @RequestBody final UpdateGenreRequest input
    );

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a genre by it identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Genre deleted successfully"),
        @ApiResponse(responseCode = "422", description = "Genre was not found"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    void deleteById(@PathVariable(name = "id") final String id);

}

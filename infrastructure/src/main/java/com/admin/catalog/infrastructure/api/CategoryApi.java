package com.admin.catalog.infrastructure.api;

import com.admin.catalog.domain.pagination.Pagination;
import com.admin.catalog.infrastructure.category.models.CategoryListResponse;
import com.admin.catalog.infrastructure.category.models.CategoryResponse;
import com.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(value = "categories")
@Tag(name = "Categories")
public interface CategoryApi {

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created with success"),
        @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest input);

    @GetMapping
    @Operation(summary = "List all categories paginated")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List all categories with success"),
        @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Pagination<CategoryListResponse> listCategories(
        @RequestParam(name = "search", required = false, defaultValue = "") final String search,
        @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
        @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
        @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
        @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a category by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
        @ApiResponse(responseCode = "422", description = "Category was not found"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    CategoryResponse getById(@PathVariable(name = "id") String id);

    @PutMapping(value = "{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a category by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully"),
        @ApiResponse(responseCode = "422", description = "Category was not found"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> updateById(@PathVariable(name = "id") String id, @RequestBody UpdateCategoryRequest input);

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a category by it's identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "422", description = "Category was not found"),
        @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    void deleteById(@PathVariable(name = "id") String id);

}

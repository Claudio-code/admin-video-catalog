package com.admin.catalog.infrastructure.category.api;

import com.admin.catalog.application.category.create.CreateCategoryOutput;
import com.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalog.application.category.retrieve.list.CategoryListOutput;
import com.admin.catalog.application.category.retrieve.list.ListCategoryUseCase;
import com.admin.catalog.application.category.update.UpdateCategoryOutput;
import com.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.exceptions.NotFoundException;
import com.admin.catalog.domain.pagination.Pagination;
import com.admin.catalog.domain.validation.Error;
import com.admin.catalog.domain.validation.handler.Notification;
import com.admin.catalog.infrastructure.ControllerTest;
import com.admin.catalog.infrastructure.api.CategoryApi;
import com.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.admin.catalog.infrastructure.category.models.UpdateCategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = CategoryApi.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private ListCategoryUseCase listCategoryUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "The worst category";
        final var expectedActive = true;
        final var input = new CreateCategoryRequest(expectedName, expectedDescription, expectedActive);

        when(createCategoryUseCase.execute(any()))
            .thenReturn(Right((CreateCategoryOutput.from("123"))));

        final var request = post("/categories")
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(input));
        final var response = mvc.perform(request)
            .andDo(print());

        response.andExpect(status().isCreated())
            .andExpect(header().string("location", "/categories/123"))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo("123")));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
            Objects.equals(expectedName, cmd.name())
            && Objects.equals(expectedDescription, cmd.description())
            && Objects.equals(expectedActive, cmd.isActive())));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "The best categories";
        final var expectedActive = true;
        final var expectedMessage = "CategoryJpaEntity: name";
        final var input = new CreateCategoryRequest(expectedName, expectedDescription, expectedActive);

        when(createCategoryUseCase.execute(any()))
            .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var request = post("/categories")
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(input));
        final var response = mvc.perform(request)
            .andDo(print());

        response.andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("location", Matchers.nullValue()))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
            Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedDescription, cmd.description())
                && Objects.equals(expectedActive, cmd.isActive())));
    }

    @Test
    public void givenValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception {
        final var expectedName = "Movies";
        final var expectedDescription = "The best movies";
        final var expectedActive = true;
        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedActive);
        final var expectedId = aCategory.getId();

        when(getCategoryByIdUseCase.execute(any()))
            .thenReturn(CategoryOutput.from(aCategory));

        final var request = get("/categories/{id}", expectedId.getValue())
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON);
        final var response = mvc.perform(request)
            .andDo(print());

        response.andExpect(status().isOk())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())))
            .andExpect(jsonPath("$.name", equalTo(expectedName)))
            .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
            .andExpect(jsonPath("$.is_active", equalTo(expectedActive)))
            .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
            .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
            .andExpect(jsonPath("$.deleted_at", equalTo(aCategory.getDeletedAt())));

        verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenAInvalidId_whenCallGetCategory_shouldReturnNotFound() throws Exception {
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.from("123");

        when(getCategoryByIdUseCase.execute(expectedId.getValue()))
            .thenThrow(NotFoundException.with(Category.class.getSimpleName(), expectedId));

        final var request = get("/categories/{id}", expectedId.getValue())
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON);
        final var response = mvc.perform(request)
            .andDo(print());


        response.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Movies";
        final var expectedDescription = "The best categories";
        final var expectedActive = true;
        final var aCommand = new UpdateCategoryRequest(expectedId, expectedName, expectedDescription, expectedActive);

        when(updateCategoryUseCase.execute(any()))
            .thenReturn(Right(UpdateCategoryOutput.from(expectedId)));

        final var request = put("/categories/{id}", expectedId)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .content(mapper.writeValueAsString(aCommand));
        final var response = mvc.perform(request)
            .andDo(print());

        response.andExpect(status().isOk())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(arg ->
            Objects.equals(expectedName, arg.name())
                && Objects.equals(expectedDescription, arg.description())
                && Objects.equals(expectedActive, arg.isActive())));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception {
        final var expectedId = "123";
        final String expectedName = null;
        final var expectedDescription = "Another category";
        final var expectedActive = true;
        final var expectedErrorCount = 1;
        final var expectedMessage = "'name' should not be null";
        final var aCommand = new UpdateCategoryRequest(expectedId, expectedName, expectedDescription, expectedActive);

        when(updateCategoryUseCase.execute(any()))
            .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var request = put("/categories/{id}", expectedId)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(aCommand));
        final var response = mvc.perform(request)
            .andDo(print());

        response.andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(arg ->
            Objects.equals(expectedName, arg.name())
                && Objects.equals(expectedDescription, arg.description())
                && Objects.equals(expectedActive, arg.isActive())));
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldReturnNotContent() throws Exception {
        final var expectedId = "123";

        doNothing()
            .when(deleteCategoryUseCase).execute(any());

        final var request = delete("/categories/{id}", expectedId)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON);
        final var response = mvc.perform(request)
            .andDo(print());

        response.andExpect(status().isNoContent());

        verify(deleteCategoryUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenValidParams_whenCallsListCategories_shouldReturnCategories() throws Exception {
        final var aCategory = Category.newCategory("Movies", null, true);
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "movies";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(CategoryListOutput.from(aCategory));

        when(listCategoryUseCase.execute(any()))
            .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = get("/categories")
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .queryParam("page", String.valueOf(expectedPage))
            .queryParam("perPage", String.valueOf(expectedPerPage))
            .queryParam("sort", expectedSort)
            .queryParam("dir", expectedDirection)
            .queryParam("search", expectedTerms);
        final var response = mvc.perform(request)
            .andDo(print());

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
            .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
            .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
            .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
            .andExpect(jsonPath("$.items[0].id", equalTo(aCategory.getId().getValue())))
            .andExpect(jsonPath("$.items[0].name", equalTo(aCategory.getName())))
            .andExpect(jsonPath("$.items[0].description", equalTo(aCategory.getDescription())))
            .andExpect(jsonPath("$.items[0].is_active", equalTo(aCategory.isActive())))
            .andExpect(jsonPath("$.items[0].created_at", equalTo(aCategory.getCreatedAt().toString())))
            .andExpect(jsonPath("$.items[0].deleted_at", equalTo(aCategory.getDeletedAt())));

        verify(listCategoryUseCase, times(1)).execute(argThat(arg ->
            Objects.equals(expectedPage, arg.page())
                && Objects.equals(expectedPerPage, arg.perPage())
                && Objects.equals(expectedDirection, arg.direction())
                && Objects.equals(expectedSort, arg.sort())
                && Objects.equals(expectedTerms, arg.terms())
        ));
    }

}


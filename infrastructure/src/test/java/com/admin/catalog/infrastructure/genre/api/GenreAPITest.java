package com.admin.catalog.infrastructure.genre.api;

import com.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.admin.catalog.application.genre.create.CreateGenreOutput;
import com.admin.catalog.application.genre.create.CreateGenreUseCase;
import com.admin.catalog.application.genre.retrieve.get.GenreOutput;
import com.admin.catalog.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.admin.catalog.application.genre.retrieve.list.ListGenreUseCase;
import com.admin.catalog.application.genre.update.UpdateGenreOutput;
import com.admin.catalog.application.genre.update.UpdateGenreUseCase;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.exceptions.NotFoundException;
import com.admin.catalog.domain.genre.Genre;
import com.admin.catalog.domain.genre.GenreID;
import com.admin.catalog.infrastructure.ControllerTest;
import com.admin.catalog.infrastructure.api.GenreApi;
import com.admin.catalog.infrastructure.genre.models.CreateGenreRequest;
import com.admin.catalog.infrastructure.genre.models.UpdateGenreRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = GenreApi.class)
class GenreAPITest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CreateGenreUseCase createGenreUseCase;
    @MockBean
    private UpdateGenreUseCase updateGenreUseCase;
    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;
    @MockBean
    private ListGenreUseCase listGenreUseCase;
    @MockBean
    private GetGenreByIdUseCase getGenreByIdUseCase;

    @Test
    void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() throws Exception {
        final var expectedName = "Action";
        final var expectedCategories = List.of("123", "321");
        final var expectedIsActive = true;
        final var expectedId = "123";
        final var aCommand = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(createGenreUseCase.execute(any()))
            .thenReturn(CreateGenreOutput.from(expectedId));

        final var aRequest = post("/genres")
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(aCommand));
        final var response = mvc.perform(aRequest)
            .andDo(print());

        response.andExpect(status().isCreated())
            .andExpect(header().string("Location", "/genres/" + expectedId))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId)));

        verify(createGenreUseCase).execute(argThat(cmd ->
            Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedCategories, cmd.categories())
                && Objects.equals(expectedIsActive, cmd.isActive())));
    }

    @Test
    void givenAValidId_whenCallsGetGenreById_shouldReturnGenre() throws Exception {
        final var expectedName = "Action";
        final var expectedCategories = List.of("321", "123");
        final var expectedIsActive = false;
        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
            .addCategories(expectedCategories.stream()
                .map(CategoryID::from)
                .toList());
        final var expectedId = aGenre.getId().getValue();

        when(getGenreByIdUseCase.execute(any()))
            .thenReturn(GenreOutput.from(aGenre));

        final var aRequest = get("/genres/{id}", expectedId)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON);
        final var response = mvc.perform(aRequest);

        response.andExpect(status().isOk())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId)))
            .andExpect(jsonPath("$.name", Matchers.equalTo(expectedName)))
            .andExpect(jsonPath("$.categories_id", Matchers.equalTo(expectedCategories)))
            .andExpect(jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)))
            .andExpect(jsonPath("$.created_at", Matchers.equalTo(aGenre.getCreatedAt().toString())))
            .andExpect(jsonPath("$.updated_at", Matchers.equalTo(aGenre.getUpdatedAt().toString())))
            .andExpect(jsonPath("$.deleted_at", Matchers.equalTo(aGenre.getDeletedAt().toString())));
        verify(getGenreByIdUseCase).execute(expectedId);
    }

    @Test
    void givenAnInvalidId_whenCallsGetGenreById_shouldReturnNotFound() throws Exception {
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        when(getGenreByIdUseCase.execute(any()))
            .thenThrow(NotFoundException.with(Genre.class.getSimpleName(), expectedId));

        final var aRequest = get("/genres/{id}", expectedId.getValue())
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON);
        final var response = mvc.perform(aRequest);

        response.andExpect(status().isNotFound())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));
        verify(getGenreByIdUseCase).execute(expectedId.getValue());
    }

    @Test
    void givenAnInvalidName_whenCallsCreateGenre_shouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedCategories = List.of("123", "321");
        final var expectedIsActive = true;
        final var expectedMessageError = "name: must not be blank";
        final var aCommand = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        final var aRequest = post("/genres")
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(aCommand));
        final var response = mvc.perform(aRequest)
            .andDo(print());

        response.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
            .andExpect(jsonPath("$.errors[0].message", Matchers.equalTo(expectedMessageError)));
    }

    @Test
    void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() throws Exception {
        final var expectedName = "Action";
        final var expectedCategories = List.of("123", "321");
        final var expectedIsActive = true;
        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = aGenre.getId().getValue();
        final var aCommand = new UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(updateGenreUseCase.execute(any()))
            .thenReturn(UpdateGenreOutput.from(aGenre));

        final var aRequest = put("/genres/{id}", expectedId)
            .contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(aCommand));
        final var response = mvc.perform(aRequest)
            .andDo(print());

        response.andExpect(status().isOk())
            .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId)));

        verify(updateGenreUseCase).execute(argThat(cmd ->
            Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedIsActive, cmd.isActive())
                && Objects.equals(expectedCategories, cmd.categories())));
    }

}

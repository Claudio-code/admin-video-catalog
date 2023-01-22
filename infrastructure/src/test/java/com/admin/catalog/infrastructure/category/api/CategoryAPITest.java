package com.admin.catalog.infrastructure.category.api;

import com.admin.catalog.application.category.create.CreateCategoryOutput;
import com.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalog.application.category.retrieve.list.ListCategoryUseCase;
import com.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.admin.catalog.infrastructure.ControllerTest;
import com.admin.catalog.infrastructure.api.CategoryApi;
import com.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static io.vavr.API.Right;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
                .andExpect(header().string("location", "/categories/123"));
    }

}


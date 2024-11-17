package com.nusiss.productservice.controller;

import com.nusiss.productservice.domain.dto.CategoryDTO;
import com.nusiss.productservice.domain.dto.CategoryPageQueryDTO;
import com.nusiss.productservice.result.PageApiResponse;
import com.nusiss.productservice.config.ApiResponse;
import com.nusiss.productservice.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void testSaveCategory() throws Exception {
        String authToken = "validAuthToken";
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("Electronics");

        // Perform the POST request
        mockMvc.perform(post("/category")
                        .header("authToken", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryName\":\"Electronics\"}"))
                .andExpect(status().isOk());

        // Verify that the save method was called
        verify(categoryService, times(1)).save(eq(authToken), eq(categoryDTO));
    }

    @Test
    void testSaveCategoryWithInvalidRequest() throws Exception {
        String authToken = "validAuthToken";

        // Perform the POST request with an empty request body
        mockMvc.perform(post("/category")
                        .header("authToken", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))  // Empty categoryDTO
                .andExpect(status().isBadRequest())  // Expecting a 400 Bad Request response
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("Invalid category data"));

        // Verify that the save method was NOT called
        verify(categoryService, times(0)).save(any(), any());
    }

    @Test
    void testQueryCategoryPage() throws Exception {
        CategoryPageQueryDTO categoryPageQueryDTO = new CategoryPageQueryDTO();
        categoryPageQueryDTO.setCategoryName("Electronics");

        PageApiResponse pageApiResponse = new PageApiResponse();
        pageApiResponse.setTotal(1);

        // Mock the pageQuery method
        when(categoryService.pageQuery(categoryPageQueryDTO)).thenReturn(pageApiResponse);

        // Perform the GET request
        mockMvc.perform(get("/category/page")
                        .param("categoryName", "Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total").value(1));

        // Verify that the pageQuery method was called
        verify(categoryService, times(1)).pageQuery(categoryPageQueryDTO);
    }

    @Test
    void testQueryCategoryPageWithInvalidRequest() throws Exception {
        // Perform the GET request with invalid parameter (e.g., empty categoryName)
        mockMvc.perform(get("/category/page")
                        .param("categoryName", ""))
                .andExpect(status().isBadRequest())  // Expecting a 400 Bad Request response
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("Invalid query parameters"));

        // Verify that pageQuery method was NOT called
        verify(categoryService, times(0)).pageQuery(any());
    }

    @Test
    void testDeleteCategory() throws Exception {
        Long categoryId = 1L;

        // Perform the DELETE request
        mockMvc.perform(delete("/category")
                        .param("id", String.valueOf(categoryId)))
                .andExpect(status().isOk());

        // Verify that deleteById method was called
        verify(categoryService, times(1)).deleteById(categoryId);
    }

    @Test
    void testDeleteCategoryWithInvalidId() throws Exception {
        // Perform the DELETE request with an empty id
        mockMvc.perform(delete("/category")
                        .param("id", ""))
                .andExpect(status().isBadRequest())  // Expecting a 400 Bad Request response
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("Invalid category ID"));

        // Verify that deleteById method was NOT called
        verify(categoryService, times(0)).deleteById(anyLong());
    }

    @Test
    void testUpdateCategory() throws Exception {
        String authToken = "validAuthToken";
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(1L);
        categoryDTO.setCategoryName("Updated Electronics");

        // Perform the PUT request
        mockMvc.perform(put("/category")
                        .header("authToken", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryId\":1,\"categoryName\":\"Updated Electronics\"}"))
                .andExpect(status().isOk());

        // Verify that update method was called
        verify(categoryService, times(1)).update(eq(authToken), eq(categoryDTO));
    }

    @Test
    void testUpdateCategoryWithInvalidRequest() throws Exception {
        String authToken = "validAuthToken";

        // Perform the PUT request with an empty request body
        mockMvc.perform(put("/category")
                        .header("authToken", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))  // Empty categoryDTO
                .andExpect(status().isBadRequest())  // Expecting a 400 Bad Request response
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("Invalid category data"));

        // Verify that the update method was NOT called
        verify(categoryService, times(0)).update(any(), any());
    }
}

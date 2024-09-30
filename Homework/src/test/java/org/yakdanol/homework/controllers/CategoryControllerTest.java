package org.yakdanol.homework.controllers;

import org.yakdanol.homework.controller.CategoryController;
import org.yakdanol.homework.model.Category;
import org.yakdanol.homework.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories_shouldReturnCategories() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setSlug("example");
        category.setName("Example Category");

        var categories = new ConcurrentHashMap<Long, Category>();
        categories.put(1L, category);

        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/v1/places/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['1'].slug").value("example"))
                .andExpect(jsonPath("$.['1'].name").value("Example Category"));
    }
}

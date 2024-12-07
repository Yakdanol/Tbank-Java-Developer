//package org.yakdanol.task5_6.controllers;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.yakdanol.task5_6.controller.CategoryController;
//import org.yakdanol.task5_6.model.entity.Category;
//import org.yakdanol.task5_6.service.CategoryService;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(CategoryController.class)
//class CategoryControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CategoryService categoryService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void getAllCategories_shouldReturnAllCategories() throws Exception {
//        // Arrange
//        ConcurrentMap<Long, Category> categories = new ConcurrentHashMap<>();
//        categories.put(1L, new Category(1L, "test-slug", "Test Category"));
//        when(categoryService.getAllCategories()).thenReturn(categories);
//
//        // Act & Assert
//        mockMvc.perform(get("/api/v1/places/categories"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(jsonPath("$.['1'].name").value("Test Category"));
//
//        verify(categoryService, times(1)).getAllCategories();
//    }
//
//    @Test
//    void getCategoryById_shouldReturnCategory() throws Exception {
//        // Arrange
//        Category category = new Category(1L, "test-slug", "Test Category");
//        when(categoryService.getCategoryById(1L)).thenReturn(category);
//
//        // Act & Assert
//        mockMvc.perform(get("/api/v1/places/categories/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(jsonPath("$.name").value("Test Category"));
//
//        verify(categoryService, times(1)).getCategoryById(1L);
//    }
//
//    @Test
//    void createCategory_shouldSaveAndReturnCategory() throws Exception {
//        // Arrange
//        Category category = new Category(1L, "test-slug", "Test Category");
//        when(categoryService.createCategory(anyLong(), any(Category.class))).thenReturn(category);
//
//        // Act & Assert
//        mockMvc.perform(post("/api/v1/places/categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"id\":1,\"slug\":\"test-slug\",\"name\":\"Test Category\"}"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(jsonPath("$.name").value("Test Category"));
//
//        verify(categoryService, times(1)).createCategory(anyLong(), any(Category.class));
//    }
//
//    @Test
//    void updateCategory_shouldUpdateAndReturnCategory() throws Exception {
//        // Arrange
//        Category category = new Category(1L, "test-slug", "Updated Category");
//        when(categoryService.updateCategory(anyLong(), any(Category.class))).thenReturn(category);
//
//        // Act & Assert
//        mockMvc.perform(put("/api/v1/places/categories/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"id\":1,\"slug\":\"test-slug\",\"name\":\"Updated Category\"}"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(jsonPath("$.name").value("Updated Category"));
//
//        verify(categoryService, times(1)).updateCategory(anyLong(), any(Category.class));
//    }
//
//    @Test
//    void deleteCategory_shouldDeleteCategory() throws Exception {
//        // Act & Assert
//        mockMvc.perform(delete("/api/v1/places/categories/1"))
//                .andExpect(status().isOk());
//
//        verify(categoryService, times(1)).deleteCategory(1L);
//    }
//}

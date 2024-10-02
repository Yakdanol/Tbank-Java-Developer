package org.yakdanol.homework.models;

import org.yakdanol.homework.model.Category;
import org.yakdanol.homework.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.yakdanol.homework.service.CategoryService;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CategoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories_shouldReturnAllCategories() {
        ConcurrentHashMap<Long, Category> categoryMap = new ConcurrentHashMap<>();
        Category category = new Category();
        category.setId(1L);
        category.setSlug("example");
        category.setName("Example Category");
        categoryMap.put(1L, category);

        when(categoryRepository.findAll()).thenReturn(categoryMap);

        var result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void createCategory_shouldSaveAndReturnCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setSlug("example");
        category.setName("Example Category");

        when(categoryRepository.save(1L, category)).thenReturn(category);

        var result = categoryService.createCategory(1L, category);

        assertEquals(category, result);
        verify(categoryRepository, times(1)).save(1L, category);
    }
}

package org.yakdanol.homework.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.yakdanol.homework.exception.CategoryNotFoundException;
import org.yakdanol.homework.exception.EmptyRepositoryException;
import org.yakdanol.homework.model.Category;
import org.yakdanol.homework.repository.CategoryRepository;
import org.yakdanol.homework.service.CategoryService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /// Позитивные сценарии

    @Test
    void getAllCategories_shouldReturnAllCategories() {
        // Arrange
        ConcurrentMap<Long, Category> categoryMap = new ConcurrentHashMap<>();
        categoryMap.put(1L, new Category(1L, "test-slug", "Test Category"));
        when(categoryRepository.findAll()).thenReturn(categoryMap);

        // Act
        ConcurrentMap<Long, Category> result = categoryService.getAllCategories();

        // Assert
        assertEquals(1, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void createCategory_shouldSaveAndReturnCategory() {
        // Arrange
        Category category = new Category(1L, "test-slug", "Test Category");
        when(categoryRepository.save(1L, category)).thenReturn(category);

        // Act
        Category result = categoryService.createCategory(1L, category);

        // Assert
        assertEquals(category, result);
        verify(categoryRepository, times(1)).save(1L, category);
    }

    @Test
    void updateCategory_shouldUpdateAndReturnCategory() {
        // Arrange
        Category category = new Category(1L, "test-slug", "Updated Category");
        when(categoryRepository.findById(1L)).thenReturn(category);
        when(categoryRepository.save(1L, category)).thenReturn(category);

        // Act
        Category updatedCategory = categoryService.updateCategory(1L, category);

        // Assert
        assertEquals("Updated Category", updatedCategory.getName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(1L, category);
    }

    @Test
    void deleteCategory_shouldDeleteCategoryById() {
        // Arrange
        Category category = new Category(1L, "test-slug", "Test Category");
        when(categoryRepository.findById(1L)).thenReturn(category);

        // Act
        categoryService.deleteCategory(1L);

        // Assert
        verify(categoryRepository, times(1)).deleteById(1L);
    }


    /// Негативные сценарии

    @Test
    void getCategoryById_shouldThrowExceptionWhenCategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(1L)).thenThrow(new CategoryNotFoundException(1L));

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(1L));
    }

    @Test
    void getAllCategories_shouldThrowExceptionWhenRepositoryIsEmpty() {
        // Arrange
        when(categoryRepository.findAll()).thenThrow(new EmptyRepositoryException("Category"));

        // Act & Assert
        assertThrows(EmptyRepositoryException.class, () -> categoryService.getAllCategories());
    }

    @Test
    void updateCategory_shouldThrowExceptionWhenCategoryNotFound() {
        // Arrange
        Category category = new Category(1L, "test-slug", "Updated Category");
        when(categoryRepository.findById(1L)).thenThrow(new CategoryNotFoundException(1L));

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(1L, category));
    }

    @Test
    void deleteCategory_shouldThrowExceptionWhenCategoryNotFound() {
        // Arrange
        doThrow(new CategoryNotFoundException(1L)).when(categoryRepository).deleteById(1L);

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(1L));
    }
}

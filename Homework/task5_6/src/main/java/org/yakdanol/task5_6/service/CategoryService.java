package org.yakdanol.task5_6.service;

import org.yakdanol.task5_6.model.Category;
import org.yakdanol.task5_6.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentMap;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ConcurrentMap<Long, Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Long id, Category category) {
        return categoryRepository.save(id, category);
    }

    public Category updateCategory(Long id, Category category) {
        // Проверка наличия категории с использованием findById
        categoryRepository.findById(id); // Если категории нет, будет выброшено CategoryNotFoundException

        return categoryRepository.save(id, category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}

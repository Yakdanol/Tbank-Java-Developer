package org.yakdanol.task5_6.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yakdanol.task5_6.dto.CategoryDTO;
import org.yakdanol.task5_6.model.entity.Category;
import org.yakdanol.task5_6.model.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        log.info("Creating new category with slug: {}", categoryDTO.getSlug());

        Category category = new Category();
        category.setSlug(categoryDTO.getSlug());
        category.setName(categoryDTO.getName());

        Category savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with ID: {}", savedCategory.getId());

        return convertToDTO(savedCategory);
    }

    public CategoryDTO findCategoryById(Long id) {
        log.info("Searching for category with ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Category not found with ID: {}", id);
                    return new EntityNotFoundException("Category not found with ID: " + id);
                });

        return new CategoryDTO(category.getId(), category.getSlug(), category.getName());
    }

    public List<CategoryDTO> findAllCategories() {
        log.info("Retrieving all categories");

        List<CategoryDTO> categories = categoryRepository.findAll().stream()
                .map(category -> new CategoryDTO(category.getId(), category.getSlug(), category.getName()))
                .collect(Collectors.toList());

        log.info("Found {} categories", categories.size());
        return categories;
    }

    public void deleteCategoryById(Long id) {
        log.info("Deleting category with ID: {}", id);
        if (!categoryRepository.existsById(id)) {
            log.error("Category not found with ID: {}", id);
            throw new EntityNotFoundException("Category not found with ID: " + id);
        }
        categoryRepository.deleteById(id);
        log.info("Category with ID: {} deleted successfully", id);
    }

    private CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getSlug(), category.getName());
    }
}

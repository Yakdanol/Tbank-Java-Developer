package org.yakdanol.task5_6.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yakdanol.task5_6.dto.CategoryDTO;
import org.yakdanol.task5_6.service.CategoryService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/places/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        log.info("Received request to create category: {}", categoryDTO);
        return categoryService.createCategory(categoryDTO);
    }

    @GetMapping("/{id}")
    public CategoryDTO getCategoryById(@PathVariable Long id) {
        log.info("Received request to get category by ID: {}", id);
        return categoryService.findCategoryById(id);
    }

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        log.info("Received request to get all categories");
        return categoryService.findAllCategories();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable Long id) {
        log.info("Received request to delete category by ID: {}", id);
        categoryService.deleteCategoryById(id);
    }
}

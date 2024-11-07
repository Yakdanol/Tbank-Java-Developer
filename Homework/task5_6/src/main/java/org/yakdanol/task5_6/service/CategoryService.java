package org.yakdanol.task5_6.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.yakdanol.task5_6.dto.CategoryDTO;
import org.yakdanol.task5_6.model.entity.Category;
import org.yakdanol.task5_6.model.repository.CategoryRepository;
import org.yakdanol.task5_6.service.observer.Subject;
import org.yakdanol.task5_6.service.observer.RepositoryObserver;
import org.yakdanol.task5_6.service.observer.CategoryHistoryObserver;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final Subject<Category> categorySubject;
    private final RestTemplate restTemplate;

    private static final String CATEGORIES_API_URL = "https://kudago.com/public-api/v1.4/place-categories/";

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, RepositoryObserver<Category> repositoryObserver, CategoryHistoryObserver historyObserver) {
        this.categoryRepository = categoryRepository;
        this.categorySubject = new Subject<>();
        categorySubject.addObserver(repositoryObserver);
        categorySubject.addObserver(historyObserver);
        this.restTemplate = new RestTemplate();
    }

    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        log.info("Creating new category with slug: {}", categoryDTO.getSlug());

        Category category = new Category();
        category.setSlug(categoryDTO.getSlug());
        category.setName(categoryDTO.getName());

        // Уведомление наблюдателей для сохранения данных
        categorySubject.notifyObservers(category);

        return convertToDTO(category);
    }

    public CategoryDTO findCategoryById(Long id) {
        log.info("Searching for category with ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Category not found with ID: {}", id);
                    return new EntityNotFoundException("Category not found with ID: " + id);
                });

        return convertToDTO(category);
    }

    public List<CategoryDTO> findAllCategories() {
        log.info("Retrieving all categories");

        List<CategoryDTO> categories = categoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        log.info("Found {} categories", categories.size());
        return categories;
    }

    @Transactional
    public void deleteCategoryById(Long id) {
        log.info("Deleting category with ID: {}", id);

        if (!categoryRepository.existsById(id)) {
            log.error("Category not found with ID: {}", id);
            throw new EntityNotFoundException("Category not found with ID: " + id);
        }

        categoryRepository.deleteById(id);
        log.info("Category with ID: {} deleted successfully", id);
    }

    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        log.info("Updating category with ID: {}", categoryDTO.getId());

        Category category = categoryRepository.findById(categoryDTO.getId())
                .orElseThrow(() -> {
                    log.error("Category not found with ID: {}", categoryDTO.getId());
                    return new EntityNotFoundException("Category not found with ID: " + categoryDTO.getId());
                });

        category.setSlug(categoryDTO.getSlug());
        category.setName(categoryDTO.getName());

        // Уведомление наблюдателей об обновлении категории
        categorySubject.notifyObservers(category);

        return convertToDTO(category);
    }

    public void initializeCategoriesFromExternalAPI() {
        log.info("Initializing categories from external API...");

        try {
            CategoryDTO[] categoriesDTO = restTemplate.getForObject(CATEGORIES_API_URL, CategoryDTO[].class);

            if (categoriesDTO != null) {
                Arrays.stream(categoriesDTO).forEach(categoryDTO -> {
                    Category category = new Category();
                    category.setSlug(categoryDTO.getSlug());
                    category.setName(categoryDTO.getName());

                    // Уведомление наблюдателей для сохранения полученных данных
                    categorySubject.notifyObservers(category);
                });
                log.info("Categories initialized successfully with {} entries.", categoriesDTO.length);
            } else {
                log.warn("No categories retrieved from external API.");
            }
        } catch (Exception e) {
            log.error("Error occurred while initializing categories from external API: {}", e.getMessage());
        }
    }

    private CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getSlug(), category.getName());
    }
}

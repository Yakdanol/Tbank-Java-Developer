package org.yakdanol.task5_6.utils;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.yakdanol.task5_6.annotation.LogExecutionTime;
import org.yakdanol.task5_6.dto.CategoryDTO;
import org.yakdanol.task5_6.dto.LocationDTO;
import org.yakdanol.task5_6.model.entity.Category;
import org.yakdanol.task5_6.model.entity.Location;
import org.yakdanol.task5_6.model.repository.CategoryRepository;
import org.yakdanol.task5_6.model.repository.LocationRepository;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Component
@Slf4j
public class KudagoInitializer {

    private static final String CATEGORIES_API_URL = "https://kudago.com/public-api/v1.4/place-categories/";
    private static final String LOCATIONS_API_URL = "https://kudago.com/public-api/v1.4/locations/";
    private static final int MAX_RETRIES = 3;

    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RestTemplate restTemplate;
    private final ExecutorService fixedThreadPool;

    public KudagoInitializer(CategoryRepository categoryRepository, LocationRepository locationRepository,
                             @Qualifier("fixedThreadPoolKudagoInitializer") ExecutorService fixedThreadPool) {
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
        this.fixedThreadPool = fixedThreadPool;
        this.restTemplate = new RestTemplate();
    }

    @PostConstruct
    @LogExecutionTime
    public void init() {
        log.info("Starting data initialization from KudaGo API...");

        CompletableFuture<Void> categoryFuture = CompletableFuture.runAsync(
                () -> RetryUtils.retryWithMaxAttempts(this::initializeCategories, MAX_RETRIES, "initializeCategories"), fixedThreadPool);

        CompletableFuture<Void> locationFuture = CompletableFuture.runAsync(
                () -> RetryUtils.retryWithMaxAttempts(this::initializeLocations, MAX_RETRIES, "initializeLocations"), fixedThreadPool);

        CompletableFuture.allOf(categoryFuture, locationFuture).join();

        log.info("Data initialization completed.");
    }

    @Transactional
    protected Void initializeCategories() {
        log.info("Fetching categories from KudaGo API...");
        CategoryDTO[] categoriesDTO = restTemplate.getForObject(CATEGORIES_API_URL, CategoryDTO[].class);

        if (categoriesDTO != null && categoriesDTO.length > 0) {
            Arrays.stream(categoriesDTO).forEach(this::saveCategory);
            log.info("Categories initialized successfully with {} entries.", categoriesDTO.length);
        } else {
            log.warn("No categories retrieved from KudaGo API.");
        }

        return null;
    }

    private void saveCategory(CategoryDTO categoryDTO) {
        try {
            Category category = new Category();
            category.setSlug(categoryDTO.getSlug());
            category.setName(categoryDTO.getName());

            categoryRepository.save(category);
            log.info("Category saved: {}", category);
        } catch (Exception e) {
            log.error("Error saving category: {}", e.getMessage());
        }
    }

    @Transactional
    protected Void initializeLocations() {
        log.info("Fetching locations from KudaGo API...");
        LocationDTO[] locationsDTO = restTemplate.getForObject(LOCATIONS_API_URL, LocationDTO[].class);

        if (locationsDTO != null && locationsDTO.length > 0) {
            Arrays.stream(locationsDTO).forEach(this::saveLocation);
            log.info("Locations initialized successfully with {} entries.", locationsDTO.length);
        } else {
            log.warn("No locations retrieved from KudaGo API.");
        }

        return null;
    }

    private void saveLocation(LocationDTO locationDTO) {
        try {
            Location location = new Location();
            location.setSlug(locationDTO.getSlug());
            location.setName(locationDTO.getName());

            locationRepository.save(location);
            log.info("Location saved: {}", location);
        } catch (Exception e) {
            log.error("Error saving location: {}", e.getMessage());
        }
    }
}

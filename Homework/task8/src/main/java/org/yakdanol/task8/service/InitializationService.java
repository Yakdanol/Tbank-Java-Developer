package org.yakdanol.task8.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import org.yakdanol.task5_6.model.repository.CategoryRepository;
import org.yakdanol.task5_6.model.repository.LocationRepository;
import org.yakdanol.task5_6.model.entity.Category;
import org.yakdanol.task5_6.model.entity.Location;

@Slf4j
@Service
public class InitializationService {

    private static final String CATEGORIES_API_URL = "https://kudago.com/public-api/v1.4/place-categories/";
    private static final String LOCATIONS_API_URL = "https://kudago.com/public-api/v1.4/locations/";

    private final ExecutorService fixedThreadPool;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public InitializationService(@Qualifier("task8FixedThreadPool") ExecutorService fixedThreadPool,
                                 CategoryRepository categoryRepository,
                                 LocationRepository locationRepository) {
        this.fixedThreadPool = fixedThreadPool;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
    }

    @PostConstruct
    public void initializeData() {
        log.info("Starting parallel initialization of data...");

        CompletableFuture<Void> categoriesInit = CompletableFuture.runAsync(this::initializeCategories, fixedThreadPool);
        CompletableFuture<Void> locationsInit = CompletableFuture.runAsync(this::initializeLocations, fixedThreadPool);
        CompletableFuture.allOf(categoriesInit, locationsInit).join();

        log.info("Data initialization completed successfully.");
    }

    private void initializeCategories() {
        try {
            Category[] categories = restTemplate.getForObject(CATEGORIES_API_URL, Category[].class);
            if (categories != null) {
                for (Category category : categories) {
                    categoryRepository.save(category.getId(), category);
                }
                log.info("Categories initialized successfully with {} entries.", categories.length);
            } else {
                log.warn("No categories retrieved from KudaGo API.");
            }
        } catch (Exception e) {
            log.error("Error during category initialization: ", e);
        }
    }

    private void initializeLocations() {
        try {
            Location[] locations = restTemplate.getForObject(LOCATIONS_API_URL, Location[].class);
            if (locations != null) {
                for (Location location : locations) {
                    locationRepository.save(location.getSlug(), location);
                }
                log.info("Locations initialized successfully with {} entries.", locations.length);
            } else {
                log.warn("No locations retrieved from KudaGo API.");
            }
        } catch (Exception e) {
            log.error("Error during location initialization: ", e);
        }
    }
}

package org.yakdanol.task5_6.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.yakdanol.task5_6.repository.CategoryRepository;
import org.yakdanol.task5_6.repository.LocationRepository;
import org.yakdanol.task5_6.model.Category;
import org.yakdanol.task5_6.model.Location;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@ComponentScan(basePackages = {"org.yakdanol.task8"})
public class KudagoInitializer {

    private static final String CATEGORIES_API_URL = "https://kudago.com/public-api/v1.4/place-categories/";
    private static final String LOCATIONS_API_URL = "https://kudago.com/public-api/v1.4/locations/";

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
    public void init() {
        log.info("Starting parallel initialization of data from KudaGo API...");

        CompletableFuture<Void> categoryFuture = CompletableFuture.runAsync(this::initializeCategories, fixedThreadPool);
        CompletableFuture<Void> locationFuture = CompletableFuture.runAsync(this::initializeLocations, fixedThreadPool);
        CompletableFuture.allOf(categoryFuture, locationFuture).join();

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

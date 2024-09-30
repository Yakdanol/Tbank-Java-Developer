package org.yakdanol.homework.utils;

import org.springframework.web.client.RestTemplate;
import org.yakdanol.homework.annotation.LogExecutionTime;
import org.yakdanol.homework.model.Category;
import org.yakdanol.homework.model.Location;
import org.yakdanol.homework.repository.CategoryRepository;
import org.yakdanol.homework.repository.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
@Slf4j
public class KudagoInitializer {

    private static final String CATEGORIES_API_URL = "https://kudago.com/public-api/v1/place-categories/";
    private static final String LOCATIONS_API_URL = "https://kudago.com/public-api/v1.4/locations/";

    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RestTemplate restTemplate;

    public KudagoInitializer(CategoryRepository categoryRepository, LocationRepository locationRepository) {
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
        this.restTemplate = new RestTemplate();
    }

    @PostConstruct
    @LogExecutionTime
    public void init() {
        log.info("Starting data initialization from KudaGo API...");

        try {
            // Инициализация категорий
            Category[] categories = restTemplate.getForObject(CATEGORIES_API_URL, Category[].class);
            if (categories != null) {
                for (Category category : categories) {
                    categoryRepository.save(category.getId(), category);
                }
                log.info("Categories initialized successfully with {} entries.", categories.length);
            } else {
                log.warn("No categories retrieved from KudaGo API.");
            }

            // Инициализация городов
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
            log.error("Error during initialization from KudaGo API: ", e);
        }

        log.info("Data initialization completed.");
    }
}

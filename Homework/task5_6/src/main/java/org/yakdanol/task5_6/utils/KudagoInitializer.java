package org.yakdanol.task5_6.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
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

@Component
@Slf4j
public class KudagoInitializer {

    private static final String CATEGORIES_API_URL = "https://kudago.com/public-api/v1.4/place-categories/";
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
            initializeCategories();

            // Инициализация локаций
            initializeLocations();

        } catch (Exception e) {
            log.error("Error during initialization from KudaGo API: ", e);
        }

        log.info("Data initialization completed.");
    }

    private void initializeCategories() {
        log.info("Fetching categories from KudaGo API...");
        try {
            CategoryDTO[] categoriesDTO = restTemplate.getForObject(CATEGORIES_API_URL, CategoryDTO[].class);

            if (categoriesDTO != null && categoriesDTO.length > 0) {
                Arrays.stream(categoriesDTO).forEach(this::saveCategory);
                log.info("Categories initialized successfully with {} entries.", categoriesDTO.length);
            } else {
                log.warn("No categories retrieved from KudaGo API.");
            }
        } catch (HttpClientErrorException e) {
            log.error("Error fetching categories from KudaGo API: {}", e.getMessage());
        }
    }

    private void saveCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setSlug(categoryDTO.getSlug());
        category.setName(categoryDTO.getName());

        // Сохранение категории в репозитории
        categoryRepository.save(category);
        log.info("Category saved: {}", category);
    }

    private void initializeLocations() {
        log.info("Fetching locations from KudaGo API...");
        try {
            LocationDTO[] locationsDTO = restTemplate.getForObject(LOCATIONS_API_URL, LocationDTO[].class);

            if (locationsDTO != null && locationsDTO.length > 0) {
                Arrays.stream(locationsDTO).forEach(this::saveLocation);
                log.info("Locations initialized successfully with {} entries.", locationsDTO.length);
            } else {
                log.warn("No locations retrieved from KudaGo API.");
            }
        } catch (HttpClientErrorException e) {
            log.error("Error fetching locations from KudaGo API: {}", e.getMessage());
        }
    }

    private void saveLocation(LocationDTO locationDTO) {
        Location location = new Location();
        location.setSlug(locationDTO.getSlug());
        location.setName(locationDTO.getName());

        // Сохранение локации в репозитории
        locationRepository.save(location);
        log.info("Location saved: {}", location);
    }
}

package org.yakdanol.task5_6.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.yakdanol.task5_6.dto.LocationDTO;
import org.yakdanol.task5_6.model.entity.Location;
import org.yakdanol.task5_6.model.repository.LocationRepository;
import org.yakdanol.task5_6.service.observer.Subject;
import org.yakdanol.task5_6.service.observer.RepositoryObserver;
import org.yakdanol.task5_6.service.observer.LocationHistoryObserver;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LocationService {

    private final LocationRepository locationRepository;
    private final Subject<Location> locationSubject;
    private final RestTemplate restTemplate;

    private static final String LOCATIONS_API_URL = "https://kudago.com/public-api/v1.4/locations/";

    @Autowired
    public LocationService(LocationRepository locationRepository, RepositoryObserver<Location> repositoryObserver, LocationHistoryObserver historyObserver) {
        this.locationRepository = locationRepository;
        this.locationSubject = new Subject<>();
        locationSubject.addObserver(repositoryObserver);
        locationSubject.addObserver(historyObserver);
        this.restTemplate = new RestTemplate();
    }

    @Transactional
    public LocationDTO createLocation(LocationDTO locationDTO) {
        log.info("Creating new location through observer pattern with slug: {}", locationDTO.getSlug());

        Location location = new Location();
        location.setSlug(locationDTO.getSlug());
        location.setName(locationDTO.getName());

        // Уведомление наблюдателей для сохранения данных
        locationSubject.notifyObservers(location);

        return convertToDTO(location);
    }

    public LocationDTO findLocationById(Long id) {
        log.info("Searching for location with ID: {}", id);

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Location not found with ID: {}", id);
                    return new EntityNotFoundException("Location not found with ID: " + id);
                });

        return convertToDTO(location);
    }

    public List<LocationDTO> findAllLocations() {
        log.info("Retrieving all locations");

        List<LocationDTO> locations = locationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        log.info("Found {} locations", locations.size());
        return locations;
    }

    @Transactional
    public void deleteLocationById(Long id) {
        log.info("Deleting location with ID: {}", id);

        if (!locationRepository.existsById(id)) {
            log.error("Location not found with ID: {}", id);
            throw new EntityNotFoundException("Location not found with ID: " + id);
        }

        locationRepository.deleteById(id);
        log.info("Location with ID: {} deleted successfully", id);
    }

    @Transactional
    public LocationDTO updateLocation(LocationDTO locationDTO) {
        log.info("Updating location through observer pattern with ID: {}", locationDTO.getId());

        Location location = locationRepository.findById(locationDTO.getId())
                .orElseThrow(() -> {
                    log.error("Location not found with ID: {}", locationDTO.getId());
                    return new EntityNotFoundException("Location not found with ID: " + locationDTO.getId());
                });

        location.setSlug(locationDTO.getSlug());
        location.setName(locationDTO.getName());

        // Уведомление наблюдателей об обновлении локации
        locationSubject.notifyObservers(location);

        return convertToDTO(location);
    }

    public void initializeLocationsFromExternalAPI() {
        log.info("Initializing locations from external API...");

        try {
            LocationDTO[] locationsDTO = restTemplate.getForObject(LOCATIONS_API_URL, LocationDTO[].class);

            if (locationsDTO != null) {
                Arrays.stream(locationsDTO).forEach(locationDTO -> {
                    Location location = new Location();
                    location.setSlug(locationDTO.getSlug());
                    location.setName(locationDTO.getName());

                    // Уведомление наблюдателей для сохранения полученных данных
                    locationSubject.notifyObservers(location);
                });
                log.info("Locations initialized successfully with {} entries.", locationsDTO.length);
            } else {
                log.warn("No locations retrieved from external API.");
            }
        } catch (Exception e) {
            log.error("Error occurred while initializing locations from external API: {}", e.getMessage());
        }
    }

    private LocationDTO convertToDTO(Location location) {
        return new LocationDTO(location.getId(), location.getSlug(), location.getName());
    }
}

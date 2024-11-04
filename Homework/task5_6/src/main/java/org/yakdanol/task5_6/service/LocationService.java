package org.yakdanol.task5_6.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.yakdanol.task5_6.dto.LocationDTO;
import org.yakdanol.task5_6.model.entity.Location;
import org.yakdanol.task5_6.model.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional
    public LocationDTO createLocation(LocationDTO locationDTO) {
        log.info("Creating new location with slug: {}", locationDTO.getSlug());

        Location location = new Location();
        location.setSlug(locationDTO.getSlug());
        location.setName(locationDTO.getName());

        Location savedLocation = locationRepository.save(location);
        log.info("Location created successfully with ID: {}", savedLocation.getId());

        return convertToDTO(savedLocation);
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
        log.info("Attempting to delete location with ID: {}", id);

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Location not found with ID: {}", id);
                    return new EntityNotFoundException("Location not found with ID: " + id);
                });

        log.info("Found location with ID: {}. Proceeding to delete associated events.", id);
        int eventCount = location.getEvents().size(); // кол-во связанных событий

        locationRepository.delete(location);

        log.info("Location with ID: {} and {} associated events were successfully deleted.", id, eventCount);
    }

    private LocationDTO convertToDTO(Location location) {
        return new LocationDTO(location.getId(), location.getSlug(), location.getName());
    }
}

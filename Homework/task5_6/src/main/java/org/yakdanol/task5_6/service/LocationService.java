package org.yakdanol.task5_6.service;

import lombok.extern.slf4j.Slf4j;
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

        return new LocationDTO(location.getId(), location.getSlug(), location.getName());
    }

    public List<LocationDTO> findAllLocations() {
        log.info("Retrieving all locations");

        List<LocationDTO> locations = locationRepository.findAll().stream()
                .map(location -> new LocationDTO(location.getId(), location.getSlug(), location.getName()))
                .collect(Collectors.toList());

        log.info("Found {} locations", locations.size());
        return locations;
    }

    public void deleteLocationById(Long id) {
        log.info("Deleting location with ID: {}", id);
        if (!locationRepository.existsById(id)) {
            log.error("Location not found with ID: {}", id);
            throw new EntityNotFoundException("Location not found with ID: " + id);
        }

        locationRepository.deleteById(id);
        log.info("Location with ID: {} deleted successfully", id);
    }

    private LocationDTO convertToDTO(Location location) {
        return new LocationDTO(location.getId(), location.getSlug(), location.getName());
    }
}

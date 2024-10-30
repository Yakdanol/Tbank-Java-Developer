package org.yakdanol.task5_6.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yakdanol.task5_6.dto.LocationDTO;
import org.yakdanol.task5_6.service.LocationService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
@Slf4j
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDTO createLocation(@RequestBody @Valid LocationDTO locationDTO) {
        log.info("Received request to create location: {}", locationDTO);
        return locationService.createLocation(locationDTO);
    }

    @GetMapping("/{id}")
    public LocationDTO getLocationById(@PathVariable Long id) {
        log.info("Received request to get location by ID: {}", id);
        return locationService.findLocationById(id);
    }

    @GetMapping
    public List<LocationDTO> getAllLocations() {
        log.info("Received request to get all locations");
        return locationService.findAllLocations();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocationById(@PathVariable Long id) {
        log.info("Received request to delete location by ID: {}", id);
        locationService.deleteLocationById(id);
    }
}

package org.yakdanol.task5_6.controller;

import lombok.extern.slf4j.Slf4j;
import org.yakdanol.task5_6.dto.LocationDTO;
import org.yakdanol.task5_6.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@Slf4j
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody @Valid LocationDTO locationDTO) {
        log.info("Received request to create location: {}", locationDTO);
        LocationDTO createdLocation = locationService.createLocation(locationDTO);
        return new ResponseEntity<>(createdLocation, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        log.info("Received request to get location by ID: {}", id);
        LocationDTO locationDTO = locationService.findLocationById(id);
        return new ResponseEntity<>(locationDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        log.info("Received request to get all locations");
        List<LocationDTO> locations = locationService.findAllLocations();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocationById(@PathVariable Long id) {
        log.info("Received request to delete location by ID: {}", id);
        locationService.deleteLocationById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

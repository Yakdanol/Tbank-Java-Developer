package org.yakdanol.task5_6.controller;

import org.yakdanol.task5_6.annotation.LogExecutionTime;
import org.yakdanol.task5_6.model.Location;
import org.yakdanol.task5_6.service.LocationService;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api/v1/locations")
@LogExecutionTime
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ConcurrentMap<String, Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{slug}")
    public Location getLocationBySlug(@PathVariable String slug) {
        return locationService.getLocationBySlug(slug);
    }

    @PostMapping
    public Location createLocation(@RequestBody Location location) {
        return locationService.createLocation(location.getSlug(), location);
    }

    @PutMapping("/{slug}")
    public Location updateLocation(@PathVariable String slug, @RequestBody Location location) {
        return locationService.updateLocation(slug, location);
    }

    @DeleteMapping("/{slug}")
    public void deleteLocation(@PathVariable String slug) {
        locationService.deleteLocation(slug);
    }
}

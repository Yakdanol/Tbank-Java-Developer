package org.yakdanol.task5_6.service;

import org.yakdanol.task5_6.model.Location;
import org.yakdanol.task5_6.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentMap;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public ConcurrentMap<String, Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationBySlug(String slug) {
        return locationRepository.findBySlug(slug);
    }

    public Location createLocation(String slug, Location location) {
        return locationRepository.save(slug, location);
    }

    public Location updateLocation(String slug, Location location) {
        // Проверка наличия локации с использованием findBySlug
        locationRepository.findBySlug(slug); // Если локации нет, будет выброшено LocationNotFoundException

        return locationRepository.save(slug, location);
    }

    public void deleteLocation(String slug) {
        locationRepository.deleteBySlug(slug);
    }
}

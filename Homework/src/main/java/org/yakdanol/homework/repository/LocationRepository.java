package org.yakdanol.homework.repository;

import org.yakdanol.homework.model.Location;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class LocationRepository {
    private final ConcurrentMap<String, Location> locationMap = new ConcurrentHashMap<>();

    public ConcurrentMap<String, Location> findAll() {
        return locationMap;
    }

    public Location findBySlug(String slug) {
        return locationMap.get(slug);
    }

    public Location save(String slug, Location location) {
        locationMap.put(slug, location);
        return location;
    }

    public void deleteBySlug(String slug) {
        locationMap.remove(slug);
    }
}

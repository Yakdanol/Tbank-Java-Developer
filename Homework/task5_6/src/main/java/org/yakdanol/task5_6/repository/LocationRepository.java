package org.yakdanol.task5_6.repository;

import org.yakdanol.task5_6.exception.EmptyRepositoryException;
import org.yakdanol.task5_6.exception.LocationNotFoundException;
import org.yakdanol.task5_6.model.Location;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class LocationRepository {
    private final ConcurrentMap<String, Location> locationMap = new ConcurrentHashMap<>();

    public ConcurrentMap<String, Location> findAll() {
        if (locationMap.isEmpty()) {
            throw new EmptyRepositoryException("Location repository");
        }
        return locationMap;
    }

    public Location findBySlug(String slug) {
        Location location = locationMap.get(slug);
        if (location == null) {
            throw new LocationNotFoundException(slug);
        }
        return location;
    }

    public Location save(String slug, Location location) {
        locationMap.put(slug, location);
        return location;
    }

    public void deleteBySlug(String slug) {
        if (!locationMap.containsKey(slug)) {
            throw new LocationNotFoundException(slug);
        }
        locationMap.remove(slug);
    }
}

package org.yakdanol.homework.repository;

import org.yakdanol.homework.exception.EmptyRepositoryException;
import org.yakdanol.homework.exception.LocationNotFoundException;
import org.yakdanol.homework.model.Location;
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

package org.yakdanol.task5_6.exception;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(String slug) {
        super("Location not found with slug: " + slug);
    }
}

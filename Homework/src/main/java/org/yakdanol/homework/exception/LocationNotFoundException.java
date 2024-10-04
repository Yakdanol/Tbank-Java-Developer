package org.yakdanol.homework.exception;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(String slug) {
        super("Location not found with slug: " + slug);
    }
}

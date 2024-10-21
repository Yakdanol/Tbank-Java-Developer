package org.yakdanol.task7.exception;

import lombok.Getter;

@Getter
public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String message) {
        super(message);
    }
}

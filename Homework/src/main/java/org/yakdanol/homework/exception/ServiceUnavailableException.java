package org.yakdanol.homework.exception;

import lombok.Getter;

@Getter
public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String message) {
        super(message);
    }
}

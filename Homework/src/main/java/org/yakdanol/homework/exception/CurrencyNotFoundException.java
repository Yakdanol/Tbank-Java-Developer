package org.yakdanol.homework.exception;

import lombok.Getter;

@Getter
public class CurrencyNotFoundException extends RuntimeException {
    private final String currencyCode;

    public CurrencyNotFoundException(String currencyCode) {
        super(String.format("Currency not found for code: %s", currencyCode));
        this.currencyCode = currencyCode;
    }
}

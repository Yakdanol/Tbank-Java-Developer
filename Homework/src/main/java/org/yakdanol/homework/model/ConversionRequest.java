package org.yakdanol.homework.model;

import lombok.Data;

@Data
public class ConversionRequest {

    private String fromCurrency;

    private String toCurrency;

    private double amount;
}

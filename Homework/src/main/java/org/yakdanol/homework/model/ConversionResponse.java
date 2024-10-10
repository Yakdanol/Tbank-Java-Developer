package org.yakdanol.homework.model;

import lombok.Data;

@Data
public class ConversionResponse {

    private String fromCurrency;

    private String toCurrency;

    private double convertedAmount;
}

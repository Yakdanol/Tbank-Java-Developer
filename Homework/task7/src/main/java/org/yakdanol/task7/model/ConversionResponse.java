package org.yakdanol.task7.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConversionResponse {

    private String fromCurrency;

    private String toCurrency;

    private double convertedAmount;
}

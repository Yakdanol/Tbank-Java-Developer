package org.yakdanol.task7.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ConversionResponse {

    private String fromCurrency;

    private String toCurrency;

    private BigDecimal convertedAmount;
}

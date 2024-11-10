package org.yakdanol.task7.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CurrencyRate {

    private String currency;

    private BigDecimal rate;
}

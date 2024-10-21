package org.yakdanol.task7.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyRate {

    private String currency;

    private double rate;
}

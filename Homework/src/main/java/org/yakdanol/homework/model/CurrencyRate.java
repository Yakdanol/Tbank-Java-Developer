package org.yakdanol.homework.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyRate {

    private String currency;

    private double rate;
}

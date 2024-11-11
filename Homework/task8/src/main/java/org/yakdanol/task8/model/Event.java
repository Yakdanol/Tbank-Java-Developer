package org.yakdanol.task8.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Event {

    private String name;

    private String description;

    private BigDecimal price;

    private String date;
}

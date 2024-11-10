package org.yakdanol.task8.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Event {

    private String name;

    private String description;

    private double price;

    private String date;
}

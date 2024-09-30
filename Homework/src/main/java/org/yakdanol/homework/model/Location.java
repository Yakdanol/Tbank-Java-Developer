package org.yakdanol.homework.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Location {
    @NonNull
    private String slug;

    @NonNull
    private String name;
}

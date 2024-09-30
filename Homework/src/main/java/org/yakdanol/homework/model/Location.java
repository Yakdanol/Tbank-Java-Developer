package org.yakdanol.homework.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Data
public class Location {
    @NonNull
    private String slug;

    @NonNull
    private String name;
}

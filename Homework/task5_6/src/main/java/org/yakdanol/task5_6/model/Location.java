package org.yakdanol.task5_6.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Location {
    @NonNull
    private String slug;

    @NonNull
    private String name;
}

package org.yakdanol.homework.model;

import lombok.*;

@NoArgsConstructor
@Data
public class Category {
    private int id;

    @NonNull
    private String slug;

    @NonNull
    private String name;

}

package org.yakdanol.homework.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category {
    private Long id;

    @NonNull
    private String slug;

    @NonNull
    private String name;

}

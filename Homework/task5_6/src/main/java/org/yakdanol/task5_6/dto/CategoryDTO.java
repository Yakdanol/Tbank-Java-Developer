package org.yakdanol.task5_6.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    private Long id;

    @NotEmpty(message = "Slug cannot be empty")
    private String slug;

    @NotEmpty(message = "Name cannot be empty")
    private String name;
}

package org.yakdanol.task5_6.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {

    private Long id;

    @NotEmpty(message = "Slug cannot be empty")
    private String slug;

    @NotEmpty(message = "Name cannot be empty")
    private String name;
}

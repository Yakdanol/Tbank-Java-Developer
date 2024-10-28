package org.yakdanol.task5_6.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    private Long id;

    @NotEmpty(message = "Event name cannot be empty")
    private String name;

    @NotNull(message = "Event date cannot be null")
    private LocalDateTime date;

    @NotNull(message = "Location ID cannot be null")
    private Long locationId;
}

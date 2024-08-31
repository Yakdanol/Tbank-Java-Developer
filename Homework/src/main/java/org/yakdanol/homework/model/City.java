package org.yakdanol.homework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {
    private String slug;
    private Coords coords;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Coords {
        private double lat;
        private double lon;
    }
}

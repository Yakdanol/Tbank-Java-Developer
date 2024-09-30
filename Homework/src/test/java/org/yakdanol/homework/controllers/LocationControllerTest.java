package org.yakdanol.homework.controllers;

import org.yakdanol.homework.controller.LocationController;
import org.yakdanol.homework.model.Location;
import org.yakdanol.homework.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationController.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllLocations_shouldReturnLocations() throws Exception {
        Location location = new Location();
        location.setSlug("msk");
        location.setName("Москва");

        var locations = new ConcurrentHashMap<String, Location>();
        locations.put("msk", location);

        when(locationService.getAllLocations()).thenReturn(locations);

        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msk.slug").value("msk"))
                .andExpect(jsonPath("$.msk.name").value("Москва"));
    }

    @Test
    void getLocationBySlug_shouldReturnLocation() throws Exception {
        Location location = new Location();
        location.setSlug("msk");
        location.setName("Москва");

        when(locationService.getLocationBySlug("msk")).thenReturn(location);

        mockMvc.perform(get("/api/v1/locations/msk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("msk"))
                .andExpect(jsonPath("$.name").value("Москва"));
    }
}

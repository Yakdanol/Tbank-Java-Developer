package org.yakdanol.task5_6.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.yakdanol.task5_6.controller.LocationController;
import org.yakdanol.task5_6.model.Location;
import org.yakdanol.task5_6.service.LocationService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    void getAllLocations_shouldReturnAllLocations() throws Exception {
        // Arrange
        ConcurrentMap<String, Location> locations = new ConcurrentHashMap<>();
        locations.put("test-slug", new Location("test-slug", "Test Location"));
        when(locationService.getAllLocations()).thenReturn(locations);

        // Act & Assert
        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.test-slug.name").value("Test Location"));

        verify(locationService, times(1)).getAllLocations();
    }

    @Test
    void getLocationBySlug_shouldReturnLocation() throws Exception {
        // Arrange
        Location location = new Location("test-slug", "Test Location");
        when(locationService.getLocationBySlug("test-slug")).thenReturn(location);

        // Act & Assert
        mockMvc.perform(get("/api/v1/locations/test-slug"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.name").value("Test Location"));

        verify(locationService, times(1)).getLocationBySlug("test-slug");
    }

    @Test
    void createLocation_shouldSaveAndReturnLocation() throws Exception {
        // Arrange
        Location location = new Location("test-slug", "Test Location");
        when(locationService.createLocation(anyString(), any(Location.class))).thenReturn(location);

        // Act & Assert
        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"slug\":\"test-slug\",\"name\":\"Test Location\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.name").value("Test Location"));

        verify(locationService, times(1)).createLocation(anyString(), any(Location.class));
    }

    @Test
    void updateLocation_shouldUpdateAndReturnLocation() throws Exception {
        // Arrange
        Location location = new Location("test-slug", "Updated Location");
        when(locationService.updateLocation(anyString(), any(Location.class))).thenReturn(location);

        // Act & Assert
        mockMvc.perform(put("/api/v1/locations/test-slug")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"slug\":\"test-slug\",\"name\":\"Updated Location\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.name").value("Updated Location"));

        verify(locationService, times(1)).updateLocation(anyString(), any(Location.class));
    }

    @Test
    void deleteLocation_shouldDeleteLocation() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/locations/test-slug"))
                .andExpect(status().isOk());

        verify(locationService, times(1)).deleteLocation("test-slug");
    }
}
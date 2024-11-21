//package org.yakdanol.task5_6.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.yakdanol.task5_6.exception.EmptyRepositoryException;
//import org.yakdanol.task5_6.exception.LocationNotFoundException;
//import org.yakdanol.task5_6.model.entity.Location;
//import org.yakdanol.task5_6.model.repository.LocationRepository;
//import org.yakdanol.task5_6.service.LocationService;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class LocationServiceTest {
//
//    @Mock
//    private LocationRepository locationRepository;
//
//    @InjectMocks
//    private LocationService locationService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    /// Позитивные сценарии
//
//    @Test
//    void getAllLocations_shouldReturnAllLocations() {
//        // Arrange
//        ConcurrentMap<String, Location> locationMap = new ConcurrentHashMap<>();
//        locationMap.put("test-slug", new Location("test-slug", "Test Location"));
//        when(locationRepository.findAll()).thenReturn(locationMap);
//
//        // Act
//        ConcurrentMap<String, Location> result = locationService.getAllLocations();
//
//        // Assert
//        assertEquals(1, result.size());
//        verify(locationRepository, times(1)).findAll();
//    }
//
//    @Test
//    void createLocation_shouldSaveAndReturnLocation() {
//        // Arrange
//        Location location = new Location("test-slug", "Test Location");
//        when(locationRepository.save("test-slug", location)).thenReturn(location);
//
//        // Act
//        Location result = locationService.createLocation("test-slug", location);
//
//        // Assert
//        assertEquals(location, result);
//        verify(locationRepository, times(1)).save("test-slug", location);
//    }
//
//    @Test
//    void updateLocation_shouldUpdateAndReturnLocation() {
//        // Arrange
//        Location location = new Location("test-slug", "Updated Location");
//        when(locationRepository.findBySlug("test-slug")).thenReturn(location);
//        when(locationRepository.save("test-slug", location)).thenReturn(location);
//
//        // Act
//        Location updatedLocation = locationService.updateLocation("test-slug", location);
//
//        // Assert
//        assertEquals("Updated Location", updatedLocation.getName());
//        verify(locationRepository, times(1)).findBySlug("test-slug");
//        verify(locationRepository, times(1)).save("test-slug", location);
//    }
//
//    @Test
//    void deleteLocation_shouldDeleteLocationBySlug() {
//        // Arrange
//        Location location = new Location("test-slug", "Test Location");
//        when(locationRepository.findBySlug("test-slug")).thenReturn(location);
//
//        // Act
//        locationService.deleteLocation("test-slug");
//
//        // Assert
//        verify(locationRepository, times(1)).deleteBySlug("test-slug");
//    }
//
//
//    /// Негативные сценарии
//
//    @Test
//    void getLocationBySlug_shouldThrowExceptionWhenLocationNotFound() {
//        // Arrange
//        when(locationRepository.findBySlug("test-slug")).thenThrow(new LocationNotFoundException("test-slug"));
//
//        // Act & Assert
//        assertThrows(LocationNotFoundException.class, () -> locationService.getLocationBySlug("test-slug"));
//    }
//
//    @Test
//    void getAllLocations_shouldThrowExceptionWhenRepositoryIsEmpty() {
//        // Arrange
//        when(locationRepository.findAll()).thenThrow(new EmptyRepositoryException("Location"));
//
//        // Act & Assert
//        assertThrows(EmptyRepositoryException.class, () -> locationService.getAllLocations());
//    }
//
//    @Test
//    void updateLocation_shouldThrowExceptionWhenLocationNotFound() {
//        // Arrange
//        Location location = new Location("test-slug", "Updated Location");
//        when(locationRepository.findBySlug("test-slug")).thenThrow(new LocationNotFoundException("test-slug"));
//
//        // Act & Assert
//        assertThrows(LocationNotFoundException.class, () -> locationService.updateLocation("test-slug", location));
//    }
//
//    @Test
//    void deleteLocation_shouldThrowExceptionWhenLocationNotFound() {
//        // Arrange
//        doThrow(new LocationNotFoundException("test-slug")).when(locationRepository).deleteBySlug("test-slug");
//
//        // Act & Assert
//        assertThrows(LocationNotFoundException.class, () -> locationService.deleteLocation("test-slug"));
//    }
//}

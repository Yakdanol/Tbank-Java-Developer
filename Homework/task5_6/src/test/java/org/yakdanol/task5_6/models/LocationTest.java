//package org.yakdanol.task5_6.models;
//
//import org.yakdanol.task5_6.model.entity.Location;
//import org.yakdanol.task5_6.model.repository.LocationRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.yakdanol.task5_6.service.LocationService;
//
//import java.util.concurrent.ConcurrentHashMap;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class LocationTest {
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
//    @Test
//    void getAllLocations_shouldReturnAllLocations() {
//        ConcurrentHashMap<String, Location> locationMap = new ConcurrentHashMap<>();
//        Location location = new Location();
//        location.setSlug("msk");
//        location.setName("Москва");
//        locationMap.put("msk", location);
//
//        when(locationRepository.findAll()).thenReturn(locationMap);
//
//        var result = locationService.getAllLocations();
//
//        assertEquals(1, result.size());
//        verify(locationRepository, times(1)).findAll();
//    }
//
//    @Test
//    void createLocation_shouldSaveAndReturnLocation() {
//        Location location = new Location();
//        location.setSlug("msk");
//        location.setName("Москва");
//
//        when(locationRepository.save("msk", location)).thenReturn(location);
//
//        var result = locationService.createLocation("msk", location);
//
//        assertEquals(location, result);
//        verify(locationRepository, times(1)).save("msk", location);
//    }
//}
//

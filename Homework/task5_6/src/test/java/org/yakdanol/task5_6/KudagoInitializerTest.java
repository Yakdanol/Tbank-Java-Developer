//package org.yakdanol.task5_6;
//
//import org.yakdanol.task5_6.model.entity.Category;
//import org.yakdanol.task5_6.model.entity.Location;
//import org.yakdanol.task5_6.model.repository.CategoryRepository;
//import org.yakdanol.task5_6.model.repository.LocationRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.web.client.RestTemplate;
//import org.yakdanol.task5_6.utils.KudagoInitializer;
//
//import static org.mockito.Mockito.*;
//
//class KudagoInitializerTest {
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @Mock
//    private LocationRepository locationRepository;
//
//    @Mock
//    private RestTemplate restTemplate;
//
//    @InjectMocks
//    private KudagoInitializer kudagoInitializer;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void init_shouldFetchAndSaveCategoriesAndLocations() {
//        Category[] categories = {new Category(123L, "airports", "Аэропорты")};
//        Location[] locations = {new Location("msk", "Москва")};
//
//        when(restTemplate.getForObject(anyString(), eq(Category[].class))).thenReturn(categories);
//        when(restTemplate.getForObject(anyString(), eq(Location[].class))).thenReturn(locations);
//
//        kudagoInitializer.init();
//
//        verify(categoryRepository, times(1)).save(123L, categories[0]);
//        verify(locationRepository, times(1)).save("msk", locations[0]);
//    }
//}

package org.yakdanol.homework.containers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;
import org.yakdanol.homework.repository.CategoryRepository;
import org.yakdanol.homework.repository.LocationRepository;
import org.yakdanol.homework.utils.KudagoInitializer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class IntegrationTest {

    @Container
    static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:3.9.1");

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private KudagoInitializer kudagoInitializer;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @BeforeAll
    static void setupWiremock() {
        wiremockServer.start();
        configureFor(wiremockServer.getHost(), wiremockServer.getFirstMappedPort());

        // Мокирование ответа для категорий
        stubFor(get(urlEqualTo("/public-api/v1.4/place-categories"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\":123,\"slug\":\"airports\",\"name\":\"Аэропорты\"}," +
                                "{\"id\":89,\"slug\":\"amusement\",\"name\":\"Развлечения\"}]")));

        // Мокирование ответа для локаций
        stubFor(get(urlEqualTo("/public-api/v1.4/locations"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"slug\":\"msk\",\"name\":\"Москва\"}," +
                                "{\"slug\":\"spb\",\"name\":\"Санкт-Петербург\"}]")));
    }

    @BeforeEach
    void cleanRepositories() {
        categoryRepository.findAll().clear();
        locationRepository.findAll().clear();
    }

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    public void testCategoryInitialization() {
        kudagoInitializer.init();

        assertThat(categoryRepository.findAll()).hasSize(54);
        assertThat(categoryRepository.findById(123L).getName()).isEqualTo("Аэропорты");
        assertThat(categoryRepository.findById(89L).getName()).isEqualTo("Развлечения");
    }

    @Test
    public void testLocationInitialization() {
        kudagoInitializer.init();

        assertThat(locationRepository.findAll()).hasSize(5);
        assertThat(locationRepository.findBySlug("msk").getName()).isEqualTo("Москва");
        assertThat(locationRepository.findBySlug("spb").getName()).isEqualTo("Санкт-Петербург");
    }
}

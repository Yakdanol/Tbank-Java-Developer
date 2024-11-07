package org.yakdanol.task5_6.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.yakdanol.task5_6.dto.CategoryDTO;
import org.yakdanol.task5_6.model.repository.CategoryRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CategoryRepository categoryRepository;

    @LocalServerPort
    private int port;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/v1/places/categories";
    }

    @Test
    void testCreateCategory() {
        CategoryDTO newCategory = new CategoryDTO(null, "test-slug", "Test Category");

        ResponseEntity<CategoryDTO> response = restTemplate.postForEntity(getBaseUrl(), newCategory, CategoryDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();

        // Проверка, что категория сохранена в БД
        assertThat(categoryRepository.findById(response.getBody().getId())).isPresent();
    }

    @Test
    @Sql(scripts = "/sql/category_data.sql")
    void testGetCategoryById() {
        Long categoryId = 500L;  // Предполагаем, что SQL-файл создаст категорию с ID 1

        ResponseEntity<CategoryDTO> response = restTemplate.getForEntity(getBaseUrl() + "/" + categoryId, CategoryDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(categoryId);
        assertThat(response.getBody().getName()).isEqualTo("Existing Category");
    }

    @Test
    @Sql(scripts = "/sql/category_data.sql")
    void testDeleteCategory() {
        Long categoryId = 500L;

        restTemplate.delete(getBaseUrl() + "/" + categoryId);

        assertThat(categoryRepository.findById(categoryId)).isNotPresent();
    }
}


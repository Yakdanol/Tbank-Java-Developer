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
import org.yakdanol.task5_6.dto.EventDTO;
import org.yakdanol.task5_6.model.repository.EventRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventControllerIntegrationTest extends IntegrationTestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EventRepository eventRepository;

    @LocalServerPort
    private int port;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/v1/events";
    }

    @Test
    @Sql(scripts = "/sql/location_data.sql") // Сначала загружаем Location для связи
    void testCreateEvent() {
        EventDTO newEvent = new EventDTO(null, "Test Event", LocalDateTime.now(), 1L);

        ResponseEntity<EventDTO> response = restTemplate.postForEntity(getBaseUrl(), newEvent, EventDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();

        // Проверка, что событие сохранено в БД
        assertThat(eventRepository.findById(response.getBody().getId())).isPresent();
    }

    @Test
    @Sql(scripts = {"/sql/location_data.sql", "/sql/event_data.sql"})
    void testGetEventById() {
        Long eventId = 1L;

        ResponseEntity<EventDTO> response = restTemplate.getForEntity(getBaseUrl() + "/" + eventId, EventDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(eventId);
        assertThat(response.getBody().getName()).isEqualTo("Existing Event");
    }

    @Test
    @Sql(scripts = {"/sql/location_data.sql", "/sql/event_data.sql"})
    void testDeleteEvent() {
        Long eventId = 1L;

        restTemplate.delete(getBaseUrl() + "/" + eventId);

        assertThat(eventRepository.findById(eventId)).isNotPresent();
    }
}


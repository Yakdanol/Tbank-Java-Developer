package org.yakdanol.task5_6.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yakdanol.task5_6.dto.EventDTO;
import org.yakdanol.task5_6.service.EventService;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDTO createEvent(@RequestBody @Valid EventDTO eventDTO) {
        log.info("Received request to create event: {}", eventDTO);
        return eventService.createEvent(eventDTO);
    }

    @GetMapping("/{id}")
    public EventDTO getEventById(@PathVariable Long id) {
        log.info("Received request to get event by ID: {}", id);
        return eventService.findEventById(id);
    }

    @GetMapping
    public List<EventDTO> searchEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {

        log.info("Searching events with filters - name: {}, locationId: {}, fromDate: {}, toDate: {}", name, locationId, fromDate, toDate);
        return eventService.searchEvents(name, locationId, fromDate, toDate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEventById(@PathVariable Long id) {
        log.info("Received request to delete event by ID: {}", id);
        eventService.deleteEventById(id);
    }
}

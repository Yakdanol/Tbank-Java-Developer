package org.yakdanol.task5_6.controller;

import lombok.extern.slf4j.Slf4j;
import org.yakdanol.task5_6.dto.EventDTO;
import org.yakdanol.task5_6.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@Slf4j
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody @Valid EventDTO eventDTO) {
        log.info("Received request to create event: {}", eventDTO);
        EventDTO createdEvent = eventService.createEvent(eventDTO);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        log.info("Received request to get event by ID: {}", id);
        EventDTO eventDTO = eventService.findEventById(id);
        return new ResponseEntity<>(eventDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> searchEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {

        log.info("Searching events with filters - name: {}, locationId: {}, fromDate: {}, toDate: {}", name, locationId, fromDate, toDate);
        List<EventDTO> events = eventService.searchEvents(name, locationId, fromDate, toDate);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventById(@PathVariable Long id) {
        log.info("Received request to delete event by ID: {}", id);
        eventService.deleteEventById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

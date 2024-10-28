package org.yakdanol.task5_6.service;

import lombok.extern.slf4j.Slf4j;
import org.yakdanol.task5_6.dto.EventDTO;
import org.yakdanol.task5_6.exception.EventNotFoundException;
import org.yakdanol.task5_6.model.entity.Event;
import org.yakdanol.task5_6.model.entity.Location;
import org.yakdanol.task5_6.model.repository.EventRepository;
import org.yakdanol.task5_6.model.repository.LocationRepository;
import org.yakdanol.task5_6.specification.EventSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public EventService(EventRepository eventRepository, LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
    }

    public EventDTO createEvent(EventDTO eventDTO) {
        log.info("Attempting to create event with name: {}", eventDTO.getName());

        Location location = locationRepository.findById(eventDTO.getLocationId())
                .orElseThrow(() -> {
                    log.error("Location not found with ID: {}", eventDTO.getLocationId());
                    return new EntityNotFoundException("Location not found with ID: " + eventDTO.getLocationId());
                });

        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDate(eventDTO.getDate());
        event.setLocation(location);

        Event savedEvent = eventRepository.save(event);
        log.info("Event created successfully with ID: {}", savedEvent.getId());

        return convertToDTO(savedEvent);
    }

    public List<EventDTO> searchEvents(String name, Long locationId, LocalDateTime fromDate, LocalDateTime toDate) {
        log.info("Searching for events with filters - name: {}, locationId: {}, fromDate: {}, toDate: {}", name, locationId, fromDate, toDate);

        Specification<Event> specification = Specification.where(null);

        if (name != null) {
            specification = specification.and(EventSpecification.hasName(name));
        }

        if (locationId != null) {
            Location location = locationRepository.findById(locationId)
                    .orElseThrow(() -> {
                        log.error("Location not found with ID: {}", locationId);
                        return new EntityNotFoundException("Location not found with ID: " + locationId);
                    });
            specification = specification.and(EventSpecification.hasLocation(location));
        }

        if (fromDate != null && toDate != null) {
            specification = specification.and(EventSpecification.dateBetween(fromDate, toDate));
        }

        List<EventDTO> events = eventRepository.findAll(specification).stream()
                .map(event -> new EventDTO(event.getId(), event.getName(), event.getDate(), event.getLocation().getId()))
                .collect(Collectors.toList());

        log.info("Found {} events matching criteria", events.size());
        return events;
    }

    public EventDTO findEventById(Long id) {
        log.info("Searching for event with ID: {}", id);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        return convertToDTO(event);
    }

    public void deleteEventById(Long id) {
        log.info("Deleting event with ID: {}", id);
        if (!eventRepository.existsById(id)) {
            log.error("Event not found with ID: {}", id);
            throw new EventNotFoundException(id);
        }

        eventRepository.deleteById(id);
        log.info("Event with ID: {} deleted successfully", id);
    }

    private EventDTO convertToDTO(Event event) {
        return new EventDTO(event.getId(), event.getName(), event.getDate(), event.getLocation().getId());
    }
}

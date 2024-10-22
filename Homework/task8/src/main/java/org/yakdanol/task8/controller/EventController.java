package org.yakdanol.task8.controller;

import lombok.RequiredArgsConstructor;
import org.yakdanol.task8.model.Event;
import org.yakdanol.task8.service.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/events")
    public CompletableFuture<Void> getFilteredEvents(@RequestParam double budget,
                                                     @RequestParam String currency,
                                                     @RequestParam(required = false) String dateFrom,
                                                     @RequestParam(required = false) String dateTo) {
        return eventService.getFilteredEventsUsingThenAcceptBoth(budget, currency, dateFrom, dateTo);
    }

    @GetMapping("/events/reactor")
    public Mono<List<Event>> getFilteredEventsWithReactor(@RequestParam double budget,
                                                          @RequestParam String currency,
                                                          @RequestParam(required = false) String dateFrom,
                                                          @RequestParam(required = false) String dateTo) {
        return eventService.getFilteredEventsWithReactor(budget, currency, dateFrom, dateTo);
    }
}

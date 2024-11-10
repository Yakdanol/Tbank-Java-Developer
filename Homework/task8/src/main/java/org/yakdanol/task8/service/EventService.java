package org.yakdanol.task8.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yakdanol.task7.service.CurrencyService;
import org.yakdanol.task8.model.Event;
import org.yakdanol.task8.util.DataFetcher;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventService {

    private final CurrencyService currencyService;
    private final DataFetcher dataFetcher;

    public EventService(CurrencyService currencyService, DataFetcher dataFetcher) {
        this.currencyService = currencyService;
        this.dataFetcher = dataFetcher;
    }

    // Реализация с использованием thenAcceptBoth
    public CompletableFuture<Void> getFilteredEventsUsingThenAcceptBoth(double budget, String currency, String dateFrom, String dateTo) {
        CompletableFuture<Double> convertedBudgetFuture = currencyService.convertToRubleAsync(budget, currency);
        CompletableFuture<List<Event>> eventsFuture = dataFetcher.fetchEvents(dateFrom, dateTo);

        return convertedBudgetFuture.thenAcceptBoth(eventsFuture, (convertedBudget, events) -> {
            List<Event> filteredEvents = events.stream()
                    .filter(event -> event.getPrice() <= convertedBudget)
                    .toList();
            log.info("Filtered {} events within budget.", filteredEvents.size());
        });
    }

    // Реализация с использованием Project Reactor
    public Mono<List<Event>> getFilteredEventsWithReactor(double budget, String currency, String dateFrom, String dateTo) {
        Mono<Double> convertedBudgetMono = Mono.fromFuture(() -> currencyService.convertToRubleAsync(budget, currency));
        Flux<Event> eventsFlux = Flux.fromStream(dataFetcher.fetchEvents(dateFrom, dateTo).join().stream());

        return convertedBudgetMono.zipWith(eventsFlux.collectList(), (convertedBudget, events) -> {
            List<Event> filteredEvents = events.stream()
                    .filter(event -> event.getPrice() <= convertedBudget)
                    .collect(Collectors.toList());
            log.info("Filtered {} events within budget using Reactor.", filteredEvents.size());
            return filteredEvents;
        });
    }
}

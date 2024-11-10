package org.yakdanol.task8.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.yakdanol.task7.service.CurrencyService;
import org.yakdanol.task8.model.Event;
import org.yakdanol.task8.util.DataFetcher;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EventServiceTest {

    private CurrencyService currencyService;
    private DataFetcher dataFetcher;
    private EventService eventService;

    @BeforeEach
    public void setUp() {
        currencyService = Mockito.mock(CurrencyService.class);
        dataFetcher = Mockito.mock(DataFetcher.class);
        eventService = new EventService(currencyService, dataFetcher);
    }

    @Test
    public void testGetFilteredEventsUsingThenAcceptBoth_Success() {
        // Mocking конвертацию бюджета
        when(currencyService.convertToRubleAsync(any(Double.class), any(String.class)))
                .thenReturn(CompletableFuture.completedFuture(1000.0));

        // Mocking получение событий
        Event event1 = new Event("Concert", "Live concert by a popular band", 500.0, "2024-10-10");
        Event event2 = new Event("Theater", "Drama performance", 1500.0, "2024-11-10");
        when(dataFetcher.fetchEvents(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(Arrays.asList(event1, event2)));

        CompletableFuture<Void> future = eventService.getFilteredEventsUsingThenAcceptBoth(1000.0, "USD", null, null);
        future.join();
        assertThat(future).isCompleted();
    }

    @Test
    public void testGetFilteredEvents_NoEventsWithinBudget() {
        // Mocking конвертацию бюджета
        when(currencyService.convertToRubleAsync(any(Double.class), any(String.class)))
                .thenReturn(CompletableFuture.completedFuture(500.0));

        // Mocking получение событий
        Event event1 = new Event("Concert", "Live concert by a popular band", 1000.0, "2024-10-10");
        when(dataFetcher.fetchEvents(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of(event1)));

        CompletableFuture<Void> future = eventService.getFilteredEventsUsingThenAcceptBoth(500.0, "USD", null, null);
        future.join();
        assertThat(future).isCompleted();
    }

    @Test
    public void testGetFilteredEvents_InvalidInput() {
        // Проверка на некорректные данные
        CompletableFuture<Void> future = eventService.getFilteredEventsUsingThenAcceptBoth(-100.0, "USD", null, null);
        assertThat(future).isCompletedExceptionally();
    }
}

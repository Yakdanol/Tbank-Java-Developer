package org.yakdanol.task8.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.yakdanol.task7.service.CurrencyService;
import org.yakdanol.task8.model.Event;
import org.yakdanol.task8.util.DataFetcher;

import java.math.BigDecimal;
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
        when(currencyService.convertToRubleAsync(any(BigDecimal.class), any(String.class)))
                .thenReturn(CompletableFuture.completedFuture(new BigDecimal("1000.00")));

        // Mocking получение событий
        Event event1 = new Event("Concert", "Live concert by a popular band", new BigDecimal("500.00"), "2024-10-10");
        Event event2 = new Event("Theater", "Drama performance", new BigDecimal("1500.00"), "2024-11-10");
        when(dataFetcher.fetchEvents(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(Arrays.asList(event1, event2)));

        CompletableFuture<Void> future = eventService.getFilteredEventsUsingThenAcceptBoth(
                new BigDecimal("1000.00"), "USD", null, null);
        future.join();
        assertThat(future).isCompleted();
    }

    @Test
    public void testGetFilteredEvents_NoEventsWithinBudget() {
        // Mocking конвертацию бюджета
        when(currencyService.convertToRubleAsync(any(BigDecimal.class), any(String.class)))
                .thenReturn(CompletableFuture.completedFuture(new BigDecimal("500.00")));

        // Mocking получение событий
        Event event1 = new Event("Concert", "Live concert by a popular band", new BigDecimal("1000.00"), "2024-10-10");
        when(dataFetcher.fetchEvents(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(List.of(event1)));

        CompletableFuture<Void> future = eventService.getFilteredEventsUsingThenAcceptBoth(
                new BigDecimal("500.00"), "USD", null, null);
        future.join();
        assertThat(future).isCompleted();
    }

    @Test
    public void testGetFilteredEvents_InvalidInput() {
        // Проверка на некорректные данные
        CompletableFuture<Void> future = eventService.getFilteredEventsUsingThenAcceptBoth(
                new BigDecimal("-100.00"), "USD", null, null);
        assertThat(future).isCompletedExceptionally();
    }
}

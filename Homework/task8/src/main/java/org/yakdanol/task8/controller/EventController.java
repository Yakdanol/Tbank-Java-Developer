package org.yakdanol.task8.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yakdanol.task8.model.Event;
import org.yakdanol.task8.service.EventService;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController("eventControllerTask8")
public class EventController {

    private final EventService eventService;

    @Operation(summary = "Получение событий на основе пользовательского бюджета и даты",
            description = "Возвращает список событий, которые соответствуют пользовательскому бюджету и укладываются в заданный диапазон дат.",
            parameters = {
                    @Parameter(name = "budget", description = "Бюджет пользователя, в котором он желает уложиться", required = true),
                    @Parameter(name = "currency", description = "Валюта пользователя (например, USD, EUR)", required = true),
                    @Parameter(name = "dateFrom", description = "Начало периода, за который пользователя интересуют события"),
                    @Parameter(name = "dateTo", description = "Конец периода, за который пользователя интересуют события")
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "События найдены и отфильтрованы по бюджету",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = @Content),
            @ApiResponse(responseCode = "404", description = "События не найдены", content = @Content),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
    })
    @GetMapping("/events")
    public CompletableFuture<Void> getFilteredEvents(@RequestParam BigDecimal budget,
                                                     @RequestParam String currency,
                                                     @RequestParam(required = false) String dateFrom,
                                                     @RequestParam(required = false) String dateTo) {
        return eventService.getFilteredEventsUsingThenAcceptBoth(budget, currency, dateFrom, dateTo);
    }

    @Operation(summary = "Получение событий с использованием Project Reactor",
            description = "Возвращает список событий, которые соответствуют пользовательскому бюджету, используя реактивный подход.",
            parameters = {
                    @Parameter(name = "budget", description = "Бюджет пользователя, в котором он желает уложиться", required = true),
                    @Parameter(name = "currency", description = "Валюта пользователя (например, USD, EUR)", required = true),
                    @Parameter(name = "dateFrom", description = "Начало периода, за который пользователя интересуют события"),
                    @Parameter(name = "dateTo", description = "Конец периода, за который пользователя интересуют события")
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "События найдены и отфильтрованы по бюджету",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса", content = @Content),
            @ApiResponse(responseCode = "404", description = "События не найдены", content = @Content),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content)
    })
    @GetMapping("/events/reactor")
    public Mono<List<Event>> getFilteredEventsWithReactor(@RequestParam BigDecimal budget,
                                                          @RequestParam String currency,
                                                          @RequestParam(required = false) String dateFrom,
                                                          @RequestParam(required = false) String dateTo) {
        return eventService.getFilteredEventsWithReactor(budget, currency, dateFrom, dateTo);
    }
}
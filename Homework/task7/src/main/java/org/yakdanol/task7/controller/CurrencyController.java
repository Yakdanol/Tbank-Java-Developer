package org.yakdanol.task7.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.yakdanol.task7.model.ConversionRequest;
import org.yakdanol.task7.model.ConversionResponse;
import org.yakdanol.task7.model.CurrencyRate;
import org.yakdanol.task7.service.CurrencyService;

@Slf4j
@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
@Tag(name = "Currencies", description = "API для работы с валютами")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Operation(summary = "Получить курс валюты по коду",
            description = "Возвращает текущий курс валюты по указанному коду (например USD).",
            parameters = {
                    @Parameter(name = "code", description = "Код валюты (например USD)", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение курса валюты",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CurrencyRate.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный код валюты", content = @Content),
            @ApiResponse(responseCode = "404", description = "Валюта не найдена", content = @Content),
            @ApiResponse(responseCode = "503", description = "Сервис недоступен", content = @Content)
    })
    @GetMapping("/rates/{code}")
    public CurrencyRate getCurrencyRate(@PathVariable String code) {
        log.info("Request received for currency rate: {}", code);
        return currencyService.getRateByCode(code);
    }

    @Operation(summary = "Конвертировать валюту",
            description = "Конвертирует сумму из одной валюты в другую на основе текущих курсов валют.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для конвертации валюты",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ConversionRequest.class))
            ))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная конвертация валюты",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ConversionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса (amount <= 0)",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Валюта не найдена", content = @Content),
            @ApiResponse(responseCode = "503", description = "Сервис недоступен", content = @Content)
    })
    @PostMapping("/convert")
    public ConversionResponse convertCurrency(@RequestBody ConversionRequest request) {
        log.info("Request received for currency conversion: {}", request);
        return currencyService.convertCurrency(request);
    }
}

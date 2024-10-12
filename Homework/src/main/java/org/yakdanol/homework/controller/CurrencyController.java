package org.yakdanol.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.yakdanol.homework.model.ConversionRequest;
import org.yakdanol.homework.model.ConversionResponse;
import org.yakdanol.homework.model.CurrencyRate;
import org.yakdanol.homework.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/currencies")
@RequiredArgsConstructor
@Tag(name = "Currencies", description = "API для работы с валютами")
public class CurrencyController {

    private final CurrencyService currencyService;

    @Operation(summary = "Получить курс валюты по коду", description = "Возвращает текущий курс валюты по указанному коду.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение курса валюты"),
            @ApiResponse(responseCode = "404", description = "Валюта не найдена")
    })
    @GetMapping("/rates/{code}")
    public CurrencyRate getCurrencyRate(@PathVariable String code) {
        log.info("Request received for currency rate: {}", code);
        return currencyService.getRateByCode(code);
    }

    @Operation(summary = "Конвертировать валюту", description = "Конвертирует указанную сумму из одной валюты в другую.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная конвертация валюты"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса (нет такой валюты или amount <= 0)"),
            @ApiResponse(responseCode = "404", description = "Валюта не найдена")
    })
    @PostMapping("/convert")
    public ConversionResponse convertCurrency(@RequestBody ConversionRequest request) {
        log.info("Request received for currency conversion: {}", request);
        return currencyService.convertCurrency(request);
    }
}

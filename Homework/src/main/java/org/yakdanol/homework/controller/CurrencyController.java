package org.yakdanol.homework.controller;

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
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/rates/{code}")
    public CurrencyRate getCurrencyRate(@PathVariable String code) {
        log.info("Request received for currency rate: {}", code);
        return currencyService.getRateByCode(code);
    }

    @PostMapping("/convert")
    public ConversionResponse convertCurrency(@RequestBody ConversionRequest request) {
        log.info("Request received for currency conversion: {}", request);
        return currencyService.convertCurrency(request);
    }
}

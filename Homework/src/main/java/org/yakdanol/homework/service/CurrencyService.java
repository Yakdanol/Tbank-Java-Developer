package org.yakdanol.homework.service;

import org.yakdanol.homework.model.ConversionRequest;
import org.yakdanol.homework.model.ConversionResponse;
import org.yakdanol.homework.model.CurrencyRate;
import org.yakdanol.homework.exception.CurrencyNotFoundException;
import org.yakdanol.homework.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final ExternalCurrencyClient currencyClient;

    @Cacheable(value = "currencyRates", key = "#code", cacheManager = "hourlyCache")
    public CurrencyRate getRateByCode(String code) {
        log.debug("Fetching rate for currency code: {}", code);
        try {
            return currencyClient.fetchRate(code);
        } catch (CurrencyNotFoundException e) {
            log.error("Currency not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error fetching currency rate for {}: {}", code, e.getMessage());
            throw new RuntimeException("Failed to fetch rate", e);
        }
    }

    public ConversionResponse convertCurrency(ConversionRequest request) {
        if (request.getAmount() <= 0) {
            log.warn("Invalid amount provided: {}", request.getAmount());
            throw new BadRequestException("Amount must be greater than 0");
        }

        try {
            CurrencyRate fromRate = getRateByCode(request.getFromCurrency());
            CurrencyRate toRate = getRateByCode(request.getToCurrency());

            double convertedAmount = (request.getAmount() / fromRate.getRate()) * toRate.getRate();
            log.info("Conversion result: {} {} to {} = {}", request.getAmount(), request.getFromCurrency(), request.getToCurrency(), convertedAmount);
            return new ConversionResponse(request.getFromCurrency(), request.getToCurrency(), convertedAmount);
        } catch (CurrencyNotFoundException e) {
            log.error("Conversion failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during currency conversion: {}", e.getMessage());
            throw new RuntimeException("Conversion failed", e);
        }
    }
}

package org.yakdanol.task7.service;

import org.yakdanol.task7.model.ConversionRequest;
import org.yakdanol.task7.model.ConversionResponse;
import org.yakdanol.task7.model.CurrencyRate;
import org.yakdanol.task7.exception.CurrencyNotFoundException;
import org.yakdanol.task7.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

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

            double convertedAmount;

            if ("RUB".equalsIgnoreCase(request.getFromCurrency())) {
                // Если исходная валюта RUB, делим сумму на курс целевой валюты
                convertedAmount = request.getAmount() / toRate.getRate();
            } else if ("RUB".equalsIgnoreCase(request.getToCurrency())) {
                // Если целевая валюта RUB, умножаем на курс исходной валюты
                convertedAmount = request.getAmount() * fromRate.getRate();
            } else {
                // Конвертация между двумя иностранными валютами
                // Переводим сумму из fromCurrency в рубли, а потом из рублей в toCurrency
                convertedAmount = (request.getAmount() * fromRate.getRate()) / toRate.getRate();
            }

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

    // Новый метод для асинхронной конвертации валюты
    public CompletableFuture<Double> convertToRubleAsync(double amount, String currencyCode) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Starting async conversion to RUB for currency: {}", currencyCode);
            try {
                CurrencyRate rate = getRateByCode(currencyCode);
                double convertedAmount = amount * rate.getRate();
                log.info("Converted {} {} to {} RUB", amount, currencyCode, convertedAmount);
                return convertedAmount;
            } catch (Exception e) {
                log.error("Error during async currency conversion: {}", e.getMessage());
                throw new RuntimeException("Currency conversion failed", e);
            }
        });
    }
}

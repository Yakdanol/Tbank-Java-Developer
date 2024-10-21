package org.yakdanol.task7;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.yakdanol.task7.exception.BadRequestException;
import org.yakdanol.task7.exception.CurrencyNotFoundException;
import org.yakdanol.task7.model.ConversionRequest;
import org.yakdanol.task7.model.ConversionResponse;
import org.yakdanol.task7.model.CurrencyRate;
import org.yakdanol.task7.service.CurrencyService;
import org.yakdanol.task7.service.ExternalCurrencyClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CurrencyServiceTest {

    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private ExternalCurrencyClient externalCurrencyClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /// Тест на успешную конвертацию USD в RUB
    @Test
    public void testConvertUsdToRub() {
        // Устанавливаем тестовые значения курсов
        when(externalCurrencyClient.fetchRate("USD")).thenReturn(new CurrencyRate("USD", 96.0686));
        when(externalCurrencyClient.fetchRate("RUB")).thenReturn(new CurrencyRate("RUB", 1.0));

        // Создаем запрос
        ConversionRequest request = new ConversionRequest("USD", "RUB", 100);
        ConversionResponse response = currencyService.convertCurrency(request);

        // Ожидаем результат конвертации
        assertEquals("USD", response.getFromCurrency());
        assertEquals("RUB", response.getToCurrency());
        assertEquals(9606.86, response.getConvertedAmount(), 0.01);
    }

    /// Тест на успешную конвертацию между двумя иностранными валютами (USD -> EUR)
    @Test
    public void testConvertUsdToEur() {
        // Устанавливаем тестовые значения курсов
        when(externalCurrencyClient.fetchRate("USD")).thenReturn(new CurrencyRate("USD", 96.0686));
        when(externalCurrencyClient.fetchRate("EUR")).thenReturn(new CurrencyRate("EUR", 105.1190));

        // Делаем запрос
        ConversionRequest request = new ConversionRequest("USD", "EUR", 100);
        ConversionResponse response = currencyService.convertCurrency(request);

        // Ожидаем результат конвертации
        assertEquals("USD", response.getFromCurrency());
        assertEquals("EUR", response.getToCurrency());
        assertEquals(91.38, response.getConvertedAmount(), 0.1);
    }

    /// Тест на ошибку, когда передан отрицательный amount
    @Test
    public void testConvertWithNegativeAmount() {
        ConversionRequest request = new ConversionRequest("USD", "RUB", -100);

        // Ожидаем исключение BadRequestException
        assertThrows(BadRequestException.class, () -> currencyService.convertCurrency(request));
    }

    /// Тест на случай, если валюта не найдена
    @Test
    public void testCurrencyNotFound() {
        when(externalCurrencyClient.fetchRate("XYZ")).thenThrow(new CurrencyNotFoundException("XYZ"));
        ConversionRequest request = new ConversionRequest("XYZ", "USD", 100);

        assertThrows(CurrencyNotFoundException.class, () -> currencyService.convertCurrency(request));
    }
}

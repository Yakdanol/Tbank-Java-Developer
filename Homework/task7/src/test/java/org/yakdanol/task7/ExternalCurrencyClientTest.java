package org.yakdanol.task7;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.yakdanol.task7.exception.CurrencyNotFoundException;
import org.yakdanol.task7.model.CurrencyRate;
import org.yakdanol.task7.service.ExternalCurrencyClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ExternalCurrencyClientTest {

    @InjectMocks
    private ExternalCurrencyClient externalCurrencyClient;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /// Тест на успешное получение курса USD
    @Test
    public void testFetchRateSuccess() {
        String mockResponse = "<ValCurs Date=\"12.10.2024\" name=\"Foreign Currency Market\">" +
                "<Valute ID=\"R01235\">" +
                "<NumCode>840</NumCode>" +
                "<CharCode>USD</CharCode>" +
                "<Nominal>1</Nominal>" +
                "<Name>Доллар США</Name>" +
                "<Value>96,0686</Value>" +
                "</Valute>" +
                "</ValCurs>";

        // Мокаем всю цепочку вызовов WebClient
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(mockResponse));

        // Вызываем метод и проверяем результат
        CurrencyRate rate = externalCurrencyClient.fetchRate("USD");

        assertNotNull(rate);
        assertEquals("USD", rate.getCurrency());

        assertEquals(new BigDecimal("100.2192"), rate.getRate());
    }

    /// Тест на случай, когда валюта не найдена
    @Test
    public void testFetchRateNotFound() {
        // Мокаем цепочку вызовов WebClient с пустым ответом
        String mockResponse = "<ValCurs Date=\"12.10.2024\" name=\"Foreign Currency Market\"></ValCurs>";

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(mockResponse));

        // Ожидаем выброс исключения
        assertThrows(CurrencyNotFoundException.class, () -> externalCurrencyClient.fetchRate("XYZ"));
    }
}

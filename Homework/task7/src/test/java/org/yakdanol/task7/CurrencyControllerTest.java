package org.yakdanol.task7;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.yakdanol.task7.exception.ServiceUnavailableException;
import org.yakdanol.task7.service.CurrencyService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    public void setUp() {
        // Указываем, что когда будет вызван метод fetchRate с "JPY", он должен выбросить исключение
        when(currencyService.getRateByCode("JPY"))
                .thenThrow(new ServiceUnavailableException("Service is unavailable, try again later."));
    }

    /// Тест для проверки ошибки валидации на отсутствующую валюту
    @Test
    public void testGetCurrencyRateInvalidCurrencyCode() throws Exception {
        mockMvc.perform(get("/currencies/rates/INVALID"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"code\":404,\"message\":\"Currency not found for code: INVALID\"}"));
    }

    /// Тест для валидации некорректного значения amount при конвертации
    @Test
    public void testConvertCurrencyWithInvalidAmount() throws Exception {
        String requestBody = "{\"fromCurrency\":\"USD\",\"toCurrency\":\"RUB\",\"amount\":-100.5}";

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"code\":400,\"message\":\"Amount must be greater than 0\"}"));
    }

    /// Тест на успешную конвертацию валют
    @Test
    public void testConvertCurrency() throws Exception {
        String requestBody = "{\"fromCurrency\":\"USD\",\"toCurrency\":\"RUB\",\"amount\":100}";

        // Пример правильного ответа для успешной конвертации
        String expectedResponse = "{\"fromCurrency\":\"USD\",\"toCurrency\":\"RUB\",\"convertedAmount\":9606.86}";

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    /// Тест на случай, если сервис ЦБ недоступен
    @Test
    public void testCurrencyServiceUnavailable() throws Exception {
        mockMvc.perform(get("/currencies/rates/JPY"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"code\":503,\"message\":\"Service is unavailable, try again later.\"}"));
    }
}

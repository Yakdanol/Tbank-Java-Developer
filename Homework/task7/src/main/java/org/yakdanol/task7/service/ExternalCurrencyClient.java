package org.yakdanol.task7.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.yakdanol.task7.exception.CurrencyNotFoundException;
import org.yakdanol.task7.exception.ServiceUnavailableException;
import org.yakdanol.task7.model.CurrencyRate;
import org.yakdanol.task7.util.XmlParser;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class ExternalCurrencyClient {

    private static final String CBR_URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private final WebClient webClient;

    public ExternalCurrencyClient() {
        this.webClient = WebClient.create(CBR_URL);
    }

    public CurrencyRate fetchRate(String code) {
        log.info("Fetching currency rate for code: {}", code);

        // Специальная обработка для RUB
        if ("RUB".equalsIgnoreCase(code)) {
            log.info("Special case: returning default rate for RUB");
            return new CurrencyRate("RUB", 1.0);
        }

        try {
            String response = webClient.get()
                    .retrieve()
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                        log.error("Service unavailable, retrying after 1 hour");
                        throw new ServiceUnavailableException("Service is unavailable, try again later.");
                    })
                    .bodyToMono(String.class)
                    .block();

            if (response == null) {
                log.error("Empty response received from CBR");
                throw new CurrencyNotFoundException(code);
            }

            Document document = XmlParser.parseXml(new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8)));
            NodeList nodeList = document.getElementsByTagName("Valute");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                String charCode = element.getElementsByTagName("CharCode").item(0).getTextContent();

                if (charCode.equalsIgnoreCase(code)) {
                    double rate = Double.parseDouble(element.getElementsByTagName("Value").item(0).getTextContent().replace(",", "."));
                    log.info("Found rate for currency {}: {}", code, rate);
                    return new CurrencyRate(charCode, rate);
                }
            }

            log.warn("Currency code {} not found in CBR response", code);
            throw new CurrencyNotFoundException(code);
        } catch (WebClientResponseException e) {
            log.error("Service unavailable: {}", e.getMessage());
            throw new ServiceUnavailableException("Service is unavailable, try again later.");
        } catch (CurrencyNotFoundException e) {
            log.error("Currency not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error fetching or parsing currency rate: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch currency rate", e);
        }
    }
}

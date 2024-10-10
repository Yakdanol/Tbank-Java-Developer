package org.yakdanol.homework.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.yakdanol.homework.exception.CurrencyNotFoundException;
import org.yakdanol.homework.model.CurrencyRate;
import org.yakdanol.homework.util.XmlParser;

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

        try {
            // Выполнение запроса к ЦБ и получение XML ответа
            String response = webClient.get()
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response == null) {
                log.error("Empty response received from CBR");
                throw new CurrencyNotFoundException(code);
            }

            // Парсинг XML ответа
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

        } catch (Exception e) {
            log.error("Error fetching or parsing currency rate: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch currency rate", e);
        }
    }
}

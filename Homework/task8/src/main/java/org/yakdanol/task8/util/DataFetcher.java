package org.yakdanol.task8.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.yakdanol.task8.model.Event;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class DataFetcher {

    private final RestTemplate restTemplate = new RestTemplate();

    public CompletableFuture<List<Event>> fetchEvents(String dateFrom, String dateTo) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String url = buildEventUrl(dateFrom, dateTo);
                Event[] events = restTemplate.getForObject(url, Event[].class);
                if (events != null) {
                    log.info("Fetched {} events from KudaGo API.", events.length);
                    return Arrays.asList(events);
                } else {
                    log.warn("No events retrieved from KudaGo API.");
                    return Collections.emptyList();
                }
            } catch (Exception e) {
                log.error("Error fetching events: ", e);
                return Collections.emptyList();
            }
        });
    }

    private String buildEventUrl(String dateFrom, String dateTo) {
        StringBuilder urlBuilder = new StringBuilder("https://kudago.com/public-api/v1.4/events/?" );
        if (dateFrom != null && !dateFrom.isEmpty()) {
            urlBuilder.append("date_from=").append(dateFrom).append("&");
        }
        if (dateTo != null && !dateTo.isEmpty()) {
            urlBuilder.append("date_to=").append(dateTo).append("&");
        }
        return urlBuilder.toString();
    }
}

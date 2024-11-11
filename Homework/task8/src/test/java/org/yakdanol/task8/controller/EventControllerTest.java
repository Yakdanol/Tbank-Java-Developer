package org.yakdanol.task8.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.yakdanol.task8.service.EventService;
import reactor.core.publisher.Mono;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Test
    public void testGetFilteredEvents_Success() throws Exception {
        when(eventService.getFilteredEventsUsingThenAcceptBoth(BigDecimal.valueOf(1000.0), "USD", null, null))
                .thenReturn(CompletableFuture.completedFuture(null));

        mockMvc.perform(get("/events")
                        .param("budget", "1000")
                        .param("currency", "USD"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetFilteredEvents_MissingParameter() throws Exception {
        mockMvc.perform(get("/events"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetFilteredEvents_InvalidBudget() throws Exception {
        mockMvc.perform(get("/events")
                        .param("budget", "-1000")
                        .param("currency", "USD"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetFilteredEventsWithReactor_Success() throws Exception {
        when(eventService.getFilteredEventsWithReactor(BigDecimal.valueOf(1000.0), "USD", null, null))
                .thenReturn(Mono.just(List.of()));

        mockMvc.perform(get("/events/reactor")
                        .param("budget", "1000")
                        .param("currency", "USD"))
                .andExpect(status().isOk());
    }
}

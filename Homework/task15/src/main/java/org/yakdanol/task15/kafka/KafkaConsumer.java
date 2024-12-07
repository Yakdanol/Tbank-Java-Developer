package org.yakdanol.task15.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.yakdanol.task15.interfaces.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer implements Consumer {

    @KafkaListener(topics = "topic", groupId = "group_id")
    @Override
    public void consumeMessage() {
        log.info("Kafka: Message received");
    }

    @Override
    public long consumeMessageAndMeasureLatency() {
        long startTime = System.nanoTime();
        log.info("Kafka: Message processed");
        return System.nanoTime() - startTime;
    }

    @Override
    public void stopConsumer() {
        log.info("Kafka: Stop consumer");
    }
}


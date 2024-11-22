package org.yakdanol.task15.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.yakdanol.task15.interfaces.Producer;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer implements Producer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void produceMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

    @Override
    public long produceMessageAndMeasureLatency(String topic, String message) {
        long startTime = System.nanoTime();
        kafkaTemplate.send(topic, message);
        return System.nanoTime() - startTime;
    }

    @Override
    public void stopProducer() {
        kafkaTemplate.getProducerFactory().getListeners().forEach(kafkaTemplate.getProducerFactory()::removeListener);
        kafkaTemplate.destroy();
        log.info("Kafka: Stop producer");
    }
}


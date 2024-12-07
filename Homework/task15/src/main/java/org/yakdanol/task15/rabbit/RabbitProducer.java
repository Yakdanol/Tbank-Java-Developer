package org.yakdanol.task15.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.yakdanol.task15.interfaces.Producer;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitProducer implements Producer {
    private final RabbitTemplate rabbitTemplate;
    private final String queueName = "queue";

    @Override
    public void produceMessage(String queueName, String message) {
        rabbitTemplate.convertAndSend(this.queueName, message);
    }

    @Override
    public long produceMessageAndMeasureLatency(String queueName, String message) {
        long startTime = System.nanoTime();
        rabbitTemplate.convertAndSend(this.queueName, message);
        return System.nanoTime() - startTime;
    }

    @Override
    public void stopProducer() {
        rabbitTemplate.getConnectionFactory().resetConnection();
        rabbitTemplate.destroy();
        log.info("RabbitMQ: Producer stopped");
    }
}


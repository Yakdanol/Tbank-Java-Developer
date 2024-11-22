package org.yakdanol.task15.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.yakdanol.task15.interfaces.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitConsumer implements Consumer {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void consumeMessage() {
        Message message = rabbitTemplate.receive("queue");
        if (message != null) {
            message.getBody();
        }
    }

    @Override
    public long consumeMessageAndMeasureLatency() {
        long startTime = System.nanoTime();
        Message message = rabbitTemplate.receive("queue");
        if (message != null) {
            message.getBody();
        }
        return System.nanoTime() - startTime;
    }

    @Override
    public void stopConsumer() {
        log.info("RabbitMQ: Stop consumer");
        rabbitTemplate.getConnectionFactory().resetConnection();
        rabbitTemplate.destroy();
    }
}


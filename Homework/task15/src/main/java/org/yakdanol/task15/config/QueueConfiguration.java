package org.yakdanol.task15.config;

import org.yakdanol.task15.interfaces.Consumer;
import org.yakdanol.task15.interfaces.Producer;

import java.util.List;

public record QueueConfiguration(
        List<Producer> queueProducerList,
        List<Consumer> consumerList,
        String message
) {

    public void run() {
        queueProducerList.forEach(queueProducer -> queueProducer.produceMessage("topic", message));
        consumerList.forEach(Consumer::consumeMessage);
    }

    public void stop() {
        queueProducerList.forEach(Producer::stopProducer);
        consumerList.forEach(Consumer::stopConsumer);
    }
}

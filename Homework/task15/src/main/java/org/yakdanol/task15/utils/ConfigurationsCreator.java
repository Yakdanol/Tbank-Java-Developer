package org.yakdanol.task15.utils;

import java.util.ArrayList;
import java.util.List;
import org.yakdanol.task15.config.MessageBrokersConfig;
import org.yakdanol.task15.config.QueueType;
import org.yakdanol.task15.interfaces.Consumer;
import org.yakdanol.task15.interfaces.Producer;
import org.yakdanol.task15.kafka.KafkaConsumer;
import org.yakdanol.task15.kafka.KafkaProducer;
import org.yakdanol.task15.config.QueueConfiguration;
import org.yakdanol.task15.rabbit.RabbitConsumer;
import org.yakdanol.task15.rabbit.RabbitProducer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;

public class ConfigurationsCreator {
    private final String messageToSend = "test";
    private ConfigurableApplicationContext context;

    public QueueConfiguration createConfiguration(int producersCount, int consumersCount, QueueType queueType) {
        context = new AnnotationConfigApplicationContext(MessageBrokersConfig.class);
        List<Producer> producers = new ArrayList<>();
        List<Consumer> consumers = new ArrayList<>();

        return switch (queueType) {
            case RABBITMQ -> createRabbitConfiguration(producersCount, consumersCount, consumers, producers);
            case KAFKA -> createKafkaConfiguration(producers, consumers);
        };
    }

    private QueueConfiguration createKafkaConfiguration(List<Producer> producers, List<Consumer> consumers) {
        KafkaTemplate<String, String> kafkaTemplate = context.getBean(KafkaTemplate.class);

        for (int i = 0; i < consumers.size(); i++) {
            consumers.add(new KafkaConsumer());
        }

        for (int i = 0; i < producers.size(); i++) {
            producers.add(new KafkaProducer(kafkaTemplate));
        }
        return new QueueConfiguration(producers, consumers, messageToSend);
    }

    private QueueConfiguration createRabbitConfiguration(int producersCount, int consumersCount, List<Consumer> consumers, List<Producer> producers) {
        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);

        for (int i = 0; i < consumersCount; i++) {
            consumers.add(new RabbitConsumer(rabbitTemplate));
        }

        for (int i = 0; i < producersCount; i++) {
            producers.add(new RabbitProducer(rabbitTemplate));
        }
        return new QueueConfiguration(producers, consumers, messageToSend);
    }

    public void clear() {
        if (context != null) {
            context.close();
            context = null;
        }
    }
}

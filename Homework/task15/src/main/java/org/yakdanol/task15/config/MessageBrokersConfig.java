package org.yakdanol.task15.config;

import org.apache.kafka.common.serialization.*;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class MessageBrokersConfig {

    // RabbitMQ Configuration
    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory("localhost");
        factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.SIMPLE);
        return factory;
    }

    @Bean
    public AmqpAdmin rabbitAdmin() {
        return new RabbitAdmin(rabbitConnectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(rabbitConnectionFactory());
    }

    @Bean
    public Queue rabbitQueue() {
        return new Queue("queue");
    }

    // Kafka Consumer Configuration
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> kafkaConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaConsumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> kafkaConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(kafkaConsumerProps());
    }

    private Map<String, Object> kafkaConsumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return props;
    }

    // Kafka Producer Configuration
    @Bean(name = "kafkaProducerFactory")
    public ProducerFactory<String, String> kafkaProducerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProducerProps());
    }

    private Map<String, Object> kafkaProducerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return props;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(
            @Qualifier("kafkaProducerFactory") ProducerFactory<String, String> kafkaProducerFactory) {
        return new KafkaTemplate<>(kafkaProducerFactory);
    }

    @Bean
    public NewTopic kafkaTopic1() {
        return new NewTopic("topic", 1, (short) 1);
    }
}

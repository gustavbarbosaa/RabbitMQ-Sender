package com.sendRabbitMQ.RabbitMQ.configs;

import lombok.Getter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Getter
@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queue.1}")
    private String registerQueueName;

    @Value("${spring.rabbitmq.queue.2}")
    private String editQueueName;

    @Value("${spring.rabbitmq.exchange.name.1}")
    private String registerExchangeName;

    @Value("${spring.rabbitmq.exchange.name.2}")
    private String editExchangeName;

    @Value("${spring.rabbitmq.routing-key.1}")
    private String registerRoutingKey;

    @Value("${spring.rabbitmq.routing-key.2}")
    private String editRoutingKey;

    @Bean
    public Queue registerQueue() {
        return new Queue(registerQueueName, true, false, false);
    }

    @Bean
    public Queue editQueue() {
        return new Queue(editQueueName, true, false, false);
    }

    @Bean
    public TopicExchange registerExchange() {
        return new TopicExchange(registerExchangeName, true, false);
    }

    @Bean
    public TopicExchange editExchange() {
        return new TopicExchange(editExchangeName, true, false);
    }

    @Bean
    public Binding bindRegisterQueue() {
        return BindingBuilder
                .bind(registerQueue())
                .to(registerExchange())
                .with(registerRoutingKey);
    }

    @Bean
    public Binding bindEditQueue() {
        return BindingBuilder
                .bind(editQueue())
                .to(editExchange())
                .with(editRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

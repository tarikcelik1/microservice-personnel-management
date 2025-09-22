package com.example.personal_backend.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PERSONEL_QUEUE = "personel.notification.queue";
    public static final String PERSONEL_EXCHANGE = "personel.notification.exchange";
    public static final String PERSONEL_ROUTING_KEY = "personel.notification.routing.key";

    @Bean
    public Queue personelQueue() {
        return QueueBuilder.durable(PERSONEL_QUEUE).build();
    }

    @Bean
    public TopicExchange personelExchange() {
        return new TopicExchange(PERSONEL_EXCHANGE);
    }

    @Bean
    public Binding personelBinding() {
        return BindingBuilder
                .bind(personelQueue())
                .to(personelExchange())
                .with(PERSONEL_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}

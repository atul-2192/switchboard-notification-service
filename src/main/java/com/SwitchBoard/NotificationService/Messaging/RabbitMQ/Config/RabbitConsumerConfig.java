package com.SwitchBoard.NotificationService.Messaging.RabbitMQ.Config;


import org.springframework.amqp.rabbit.annotation.EnableRabbit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;



@Configuration
@EnableRabbit
@Profile("prod")
public class RabbitConsumerConfig {

    @Value("${rabbitmq.queue.otp}")
    private String otpQueue;

    @Value("${rabbitmq.queue.onboarding}")
    private String onboardingQueue;

    @Bean
    public Queue otpQueue() {
        return new Queue(otpQueue, true);
    }

    @Bean
    public Queue onboardingQueue() {
        return new Queue(onboardingQueue, true);
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}

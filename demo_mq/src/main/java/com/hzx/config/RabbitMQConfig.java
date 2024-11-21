package com.hzx.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Configuration
@EnableRabbit
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Setter
@Getter
@Slf4j
public class RabbitMQConfig {

    private String host;
    private int port;
    private String username;
    private String password;
    private String virtualHost;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        log.info("RabbitMQ ConnectionFactory configured successfully.");
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        log.info("RabbitTemplate configured successfully.");
        return rabbitTemplate;
    }


    @Bean
    public RabbitAdmin directAdmin(ConnectionFactory connectionFactory) {
        final RabbitAdmin admin = new RabbitAdmin(connectionFactory);

        // direct
        admin.declareQueue(directQueue1());
        admin.declareQueue(directQueue2());
        admin.declareExchange(directExchange());
//        admin.declareBinding(directBinding1());
//        admin.declareBinding(directBinding2());

        // fanout
        admin.declareQueue(fanoutQueue1());
        admin.declareQueue(fanoutQueue2());
        admin.declareExchange(fanoutExchange());
        admin.declareBinding(fanoutBinding1());
        admin.declareBinding(fanoutBinding2());

        // topic
        admin.declareQueue(topicQueue1());
        admin.declareQueue(topicQueue2());
        admin.declareExchange(topicExchange());
        admin.declareBinding(topicBinding1());
        admin.declareBinding(topicBinding2());

        return admin;
    }

    // direct
    @Bean
    public Queue directQueue1() {
        return new Queue("direct.queue1", true);
    }

    @Bean
    public Queue directQueue2() {
        return new Queue("direct.queue2", true);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("direct.exchange");
    }

    @Bean
    public Binding directBinding1() {
        return new Binding("direct.queue1", Binding.DestinationType.QUEUE, "direct.exchange", "key1", null);
    }

    @Bean
    public Binding directBinding2() {
        return new Binding("direct.queue2", Binding.DestinationType.QUEUE, "direct.exchange", "key2", null);
    }

    // fanout
    @Bean
    public Queue fanoutQueue1() {
        return new Queue("fanout.queue1", true);
    }

    @Bean
    public Queue fanoutQueue2() {
        return new Queue("fanout.queue2", true);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanout.exchange");
    }

    @Bean
    public Binding fanoutBinding1() {
        return new Binding("fanout.queue1", Binding.DestinationType.QUEUE, "fanout.exchange", "", null);
    }

    @Bean
    public Binding fanoutBinding2() {
        return new Binding("fanout.queue2", Binding.DestinationType.QUEUE, "fanout.exchange", "", null);
    }



    // topic
    @Bean
    public Queue topicQueue1() {
        return new Queue("topic.queue1", true);
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue("topic.queue2", true);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topic.exchange");
    }

    @Bean
    public Binding topicBinding1() {
        return new Binding("topic.queue1", Binding.DestinationType.QUEUE, "topic.exchange", "key1.*", null);
    }

    @Bean
    public Binding topicBinding2() {
        return new Binding("topic.queue2", Binding.DestinationType.QUEUE, "topic.exchange", "*.key2", null);
    }

}

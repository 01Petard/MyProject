package com.hzx.direct;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DirectProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String routingKey, String message) {
        rabbitTemplate.convertAndSend("direct.exchange", routingKey, message);
    }
}
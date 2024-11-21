package com.hzx.topic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TopicConsumer {

    @RabbitListener(queues = "topic.queue1")
    public void receiveQueue1(String message) {
        log.info("Received message from topic.queue1: {}", message);
    }

    @RabbitListener(queues = "topic.queue2")
    public void receiveQueue2(String message) {
        log.info("Received message from topic.queue2: {}", message);
    }
}
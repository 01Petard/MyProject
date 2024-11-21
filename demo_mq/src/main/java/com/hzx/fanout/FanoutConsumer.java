package com.hzx.fanout;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FanoutConsumer {

    @RabbitListener(queues = "fanout.queue1")
    public void receiveQueue1(String message) {
        log.info("Received message from fanout.queue1: {}", message);
    }

    @RabbitListener(queues = "fanout.queue2")
    public void receiveQueue2(String message) {
        log.info("Received message from fanout.queue2: {}", message);
    }
}
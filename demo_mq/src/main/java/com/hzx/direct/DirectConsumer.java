package com.hzx.direct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DirectConsumer {

//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "direct.queue1"),
//            exchange = @Exchange(value = "direct.exchange", type = "direct"),
//            key = "key1"))

    @RabbitListener(queues = "direct.queue1")
    public void receiveQueue1(String message) {
        log.info("Received message from direct.queue1: {}", message);
    }

//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "direct.queue2"),
//            exchange = @Exchange(value = "direct.exchange", type = "direct"),
//            key = "key2"))

    @RabbitListener(queues = "direct.queue2")
    public void receiveQueue2(String message) {
        log.info("Received message from direct.queue2: {}", message);
    }
}

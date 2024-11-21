package com.hzx;

import com.hzx.direct.DirectProducer;
import com.hzx.fanout.FanoutProducer;
import com.hzx.topic.TopicProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@SpringBootTest
@RunWith(SpringRunner.class)
public class MyTest {

    @Resource
    public RabbitTemplate rabbitTemplate;

    @Test
    public void test() {
        rabbitTemplate.convertAndSend("e.test", "q.test", "hello, rabbitmq!");
    }


    @Autowired
    private DirectProducer directProducer;

    @Autowired
    private FanoutProducer fanoutProducer;

    @Autowired
    private TopicProducer topicProducer;

    @Test
    public void testDirectExchange() {
//        directProducer.sendMessage("key1", "Hello, direct.queue1!");
//        directProducer.sendMessage("key2", "Hello, direct.queue2!");
        rabbitTemplate.convertAndSend("direct.exchange", "key1" , "Hello, direct.queue1!");
        rabbitTemplate.convertAndSend("direct.exchange", "key2" , "Hello, direct.queue2!");
    }

    @Test
    public void testFanoutExchange() {
//        fanoutProducer.sendMessage("Hello, fanout queues!");
        rabbitTemplate.convertAndSend("fanout.exchange","", "Hello, fanout queues!");

    }

    @Test
    public void testTopicExchange() {
//        topicProducer.sendMessage("key1.a", "Hello, topic.queue1!");
//        topicProducer.sendMessage("b.key2", "Hello, topic.queue2!");
        rabbitTemplate.convertAndSend("topic.exchange", "key1.a", "Hello, topic.queue1!");
        rabbitTemplate.convertAndSend("topic.exchange", "b.key2", "Hello, topic.queue1!");
    }

}

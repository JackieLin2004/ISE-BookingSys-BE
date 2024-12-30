package com.krowfeather.customer;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class CustomerApplicationTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    void contextLoads() {
        String queueName = "proposal_generate.queue";
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("customerId", 1);
        rabbitTemplate.convertAndSend(queueName,  data);
    }

}

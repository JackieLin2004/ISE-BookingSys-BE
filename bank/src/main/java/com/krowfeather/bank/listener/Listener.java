package com.krowfeather.bank.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krowfeather.bank.service.OrderService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Listener {
    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    public Listener(ObjectMapper objectMapper, OrderService orderService) {
        this.objectMapper = objectMapper;
        this.orderService = orderService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "payment_bank.queue1", durable = "true"),
            exchange = @Exchange(name = "kf.fanout", type = ExchangeTypes.FANOUT)
    ))
    public void paymentListener(String message) throws JsonProcessingException {
        Map<String, Object> data = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
        System.err.println("PAYMENT SERVICE Received message: " + data);
        this.orderService.createOrder(data.get("pid"),data.get("cid"),data.get("price"));
    }
}

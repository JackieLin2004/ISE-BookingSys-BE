package com.krowfeather.bank.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krowfeather.bank.service.OrderService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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
            value = @Queue(name = "pay_bank.queue", durable = "true"),
            exchange = @Exchange(name = "kf.fanout", type = ExchangeTypes.FANOUT)
    ))
    public void paymentListener(Message message) throws InterruptedException {
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        Map<String, Object> data = (Map<String, Object>) jackson2JsonMessageConverter.fromMessage(message, Map.class);
        System.err.println("PAYMENT SERVICE Received message: " + data);
        this.orderService.createOrder((Integer) data.get("price"), (Integer) data.get("pid"), (Integer) data.get("cid"));
    }
}

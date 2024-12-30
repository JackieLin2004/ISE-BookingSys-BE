package com.krowfeather.bookingsystem.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krowfeather.bookingsystem.service.ProposalService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.Map;

@Component
public class Listener {
    private final ProposalService proposalService;
    private final ObjectMapper objectMapper;
    public Listener(ProposalService proposalService, ObjectMapper objectMapper) {
        this.proposalService = proposalService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "proposal_generate.queue", durable = "true"),
            exchange = @Exchange(name = "kf.fanout", type = ExchangeTypes.FANOUT)
    ))
    public void ProposalGenerateListener(Message message) throws JsonProcessingException {
        System.out.println("Received raw message: " + message);
        System.out.println("PROPOSAL GENERATE SERVICE Received message: " + message);
        Jackson2JsonMessageConverter jackson2JsonMessageConverter =new Jackson2JsonMessageConverter();
        try{
            Map<String, Object> data = (Map<String, Object>) jackson2JsonMessageConverter.fromMessage(message);
            this.proposalService.generateProposal(data.get("depart"),data.get("destination"),data.get("cid"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "proposal_withdraw.queue", durable = "true"),
            exchange = @Exchange(name = "kf.fanout", type = ExchangeTypes.FANOUT)
    ))
    public void ProposalWithdrawListener(String message) throws JsonProcessingException {
        Map<String, Object> data = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
        System.out.println("PROPOSAL WITHDRAW SERVICE Received message: " + data);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "proposal_accept.queue", durable = "true"),
            exchange = @Exchange(name = "kf.fanout", type = ExchangeTypes.FANOUT)
    ))
    public void ProposalAcceptListener(String message) throws JsonProcessingException {
        Map<String, Object> data = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
        System.out.println("PROPOSAL ACCEPT SERVICE Received message: " + data);
        proposalService.processAccept(data.get("pid"));
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "proposal_get.queue", durable = "true"),
            exchange = @Exchange(name = "kf.fanout", type = ExchangeTypes.FANOUT)
    ))
    public void ProposalGetListener(Message message) throws JsonProcessingException {
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        Map<String, Object> data = (Map<String, Object>) jackson2JsonMessageConverter.fromMessage(message, Map.class);
        System.out.println("PROPOSAL GET SERVICE Received message: " + data);
        proposalService.getAllWaitingProposal((Integer) data.get("id"));
    }
}

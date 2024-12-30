package com.krowfeather.customer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krowfeather.customer.entity.Customer;
import com.krowfeather.customer.mapper.CustomerMapper;
import com.krowfeather.customer.service.CustomerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.Map;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {
    private final CustomerMapper customerMapper;
    private final RabbitTemplate rabbitTemplate;
    public CustomerServiceImpl(CustomerMapper customerMapper, RabbitTemplate rabbitTemplate) {
        this.customerMapper = customerMapper;
        this.rabbitTemplate = rabbitTemplate;
    }
    @Override
    public Customer getByUsername(String username) {
        return this.customerMapper.getByUsername(username);
    }

    @Override
    public void getProposal(Integer id, String depart, String destination) {
        Map<String, Object> data = Map.of("depart", depart, "destination", destination, "cid", id);
        String queueName = "proposal_generate.queue";
        rabbitTemplate.convertAndSend(queueName, data);
        System.out.println("send request");
    }

    @Override
    public void withdraw(Integer pid) {
        Map<String, Object> data = Map.of("pid", pid);
        String queueName = "proposal_withdraw.queue";
        rabbitTemplate.convertAndSend(queueName, data);
        System.out.println("send withdraw request");
    }

    @Override
    public void accept(Integer pid) {
        Map<String,Object> data = Map.of("pid", pid);
        String queueName = "proposal_accept.queue";
        rabbitTemplate.convertAndSend(queueName, data);
        System.out.println("send accept request");
    }

    @Override
    public String allWaitingProposal(Integer id) {
        Map<String, Object> data = Map.of("id", id);
        this.rabbitTemplate.convertAndSend("proposal_get.queue",data);
        return "";
    }

    @Override
    public String allWithdrawedProposal(Integer id) {
        Map<String, Object> data = Map.of("id", id);
        this.rabbitTemplate.convertAndSend("proposal_get_withdraw.queue",data);
        return "";
    }

    @Override
    public String allAcceptedProposal(Integer id) {
        Map<String, Object> data = Map.of("id", id);
        this.rabbitTemplate.convertAndSend("proposal_get_accept.queue",data);
        return "";
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        if (this.count() > 0) {
            return;
        }
        Customer customer = new Customer();
        customer.setUsername("customer1");
        String password = "123";
        SimpleHash simpleHash = new SimpleHash("MD5", password, "salt", 3);
        customer.setPassword(simpleHash.toHex());
        this.save(customer);
        Customer customer1 = new Customer();
        customer1.setUsername("customer2");
        String password1 = "1234";
        SimpleHash simpleHash1 = new SimpleHash("MD5", password1, "salt", 3);
        customer1.setPassword(simpleHash1.toHex());
        this.save(customer1);
    }
}

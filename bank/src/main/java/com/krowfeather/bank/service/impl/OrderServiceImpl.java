package com.krowfeather.bank.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krowfeather.bank.entity.Orders;
import com.krowfeather.bank.entity.factory.AliFactory;
import com.krowfeather.bank.entity.factory.IPaymentFactory;
import com.krowfeather.bank.entity.factory.WechatFactory;
import com.krowfeather.bank.entity.pay.IPayment;
import com.krowfeather.bank.mapper.OrderMapper;
import com.krowfeather.bank.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService{
    private final OrderMapper orderMapper;
    private final RabbitTemplate rabbitTemplate;

    public OrderServiceImpl(OrderMapper orderMapper, RabbitTemplate rabbitTemplate) {
        this.orderMapper = orderMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void createOrder(Object pid, Object cid, Object price) {
//        this.orderMapper.insertOrder((Integer) price, (Integer) pid, (Integer) cid);
        Orders order = new Orders();
        order.setPid((Integer) pid);
        order.setPrice((Integer) price);
        orderMapper.insertEntity((Integer) pid, (Integer) price);
    }

    @Transactional
    @Override
    public String pay(int pid, int type) {
        IPaymentFactory factory;
        if (type == 0) {
            factory = new WechatFactory();
        } else if (type == 1) {
            factory = new AliFactory();
        } else {
            throw new IllegalArgumentException("Invalid payment type");
        }
        IPayment payment = factory.createPayment();
        payment.pay();

        UpdateWrapper<Orders> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("pid", pid)
                .set("`status`", 1);
        orderMapper.update(null, updateWrapper);

        Map<String, Object> data = Map.of("pid", pid);
        this.rabbitTemplate.convertAndSend("pay_notify2bank.queue", data);
        return "Payment successful";
    }
}
